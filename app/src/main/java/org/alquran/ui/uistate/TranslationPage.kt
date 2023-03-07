package org.alquran.ui.uistate

import androidx.compose.ui.text.font.FontFamily
import arg.quran.models.quran.VerseKey
import arg.quran.models.quran.VerseTranslation
import org.alquran.audio.models.AudioState

data class TranslationPage(
  override val page: Int = 0,
  override val header: Header,
  val items: List<Row> = emptyList(),
  val scrollIndex: Int,
  val firstItem: Boolean,
  override val fontFamily: FontFamily,
) : QuranPageItem() {
  sealed class Row(open val key: Any)

  data class Surah(val number: Int, val name: String) : Row(number)

  data class Verse(
    val id: Int,
    val verseKey: VerseKey,
    val words: List<WordUiState>,
    val isBookmarked: Boolean,
    val highLighted: Boolean,
    val audioState: AudioState,
  ) : Row("Verse(${verseKey})")

  data class Translation(
    val authorName: String,
    val translation: VerseTranslation,
    val audioState: AudioState,
    val isBookmarked: Boolean,
    val highLighted: Boolean
  ) : Row("Translation(${translation.id})")

  data class AyahToolbar(
    val verseKey: VerseKey,
    val isBookmarked: Boolean,
    val highLighted: Boolean,
    val audioState: AudioState,
  ) : Row("AyahToolbar($verseKey)")

  class Divider(key: Any) : Row(key)
}

fun List<TranslationPage.Row>.indexOfItem(sura: Int, aya: Int): Int {
  val index = indexOfFirst { model ->
    when (model) {
      is TranslationPage.Surah -> model.number == sura && aya == 1
      is TranslationPage.Verse -> model.verseKey.aya == aya && model.verseKey.sura == sura
      else -> false
    }
  }
  return if (index > 0) index + 1 else index
}

val TranslationPage.Translation.verseKey get() = translation.key