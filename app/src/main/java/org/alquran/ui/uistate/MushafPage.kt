package org.alquran.ui.uistate

import androidx.compose.ui.text.font.FontFamily

data class MushafPage(
  override val page: Int = 0,
  override val header: Header,
  override val fontFamily: FontFamily,
  val lines: List<Line> = emptyList(),
) : QuranPageItem() {

  sealed class Line

  data class ChapterLine(
    val line: Int,
    val sura: Int,
  ) : Line()

  data class TextLine(
    val words: List<WordUiState>,
    val line: Int,
  ) : Line()

  data class Basmallah(val line: Int) : Line()

  data class Blank(val line: Int) : Line()
}