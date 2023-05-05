package org.alquran.ui.uistate

import androidx.compose.ui.text.font.FontFamily
import arg.quran.models.quran.VerseKey
import org.alquran.ui.screen.pager.AyahEvent

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
    val sura: Int,
    val ayah: Int,
    val text: String,
    val isBookmarked: Boolean,
    val highLighted: Boolean,
    val isPlaying: Boolean,
  ) : Row("Verse($sura:$ayah)")

  data class Translation(
    val authorName: String,
    val sura: Int,
    val ayah: Int,
    val text: String,
    val isPlaying: Boolean,
    val isBookmarked: Boolean,
    val highLighted: Boolean
  ) : Row("Translation($sura:$ayah)")

  data class AyahToolbar(
    val verseKey: VerseKey,
    val isBookmarked: Boolean,
    val highLighted: Boolean,
    val isPlaying: Boolean,
  ) : Row("AyahToolbar($verseKey)")

  class Divider(key: Any) : Row(key)
}

fun List<TranslationPage.Row>.indexOfItem(sura: Int, aya: Int): Int {
  val index = indexOfFirst { model ->
    when (model) {
      is TranslationPage.Surah -> model.number == sura && aya == 1
      is TranslationPage.Verse -> model.ayah == aya && model.sura == sura
      else -> false
    }
  }
  return if (index > 0) index + 1 else index
}

val TranslationPage.Translation.verseKey get() = VerseKey(sura, ayah)