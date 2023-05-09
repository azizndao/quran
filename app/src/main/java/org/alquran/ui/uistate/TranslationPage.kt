package org.alquran.ui.uistate

import androidx.compose.ui.text.font.FontFamily
import arg.quran.models.quran.VerseKey

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
    val suraAyah: VerseKey,
    val text: String,
    val isBookmarked: Boolean,
    val highLighted: Boolean,
    val isPlaying: Boolean,
    val translations: List<TranslatedVerse>
  ) : Row("Verse($suraAyah)")

  data class TranslatedVerse(val text: String, val authorName: String)

  class Divider(key: Any) : Row(key)
}

fun List<TranslationPage.Row>.indexOfItem(sura: Int, aya: Int): Int {
  val index = indexOfFirst { model ->
    when (model) {
      is TranslationPage.Surah -> model.number == sura && aya == 1
      is TranslationPage.Verse -> model.suraAyah.aya == aya && model.suraAyah.sura == sura
      else -> false
    }
  }
  return if (index > 0) index + 1 else index
}