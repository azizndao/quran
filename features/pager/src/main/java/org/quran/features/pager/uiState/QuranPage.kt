package org.quran.features.pager.uiState

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class QuranPage(
  override val page: Int = 0,
  override val header: Header,
  val lines: PersistentList<Line> = persistentListOf(),
) : PageItem() {

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