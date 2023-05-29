package org.quran.features.pager.previews

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import arg.quran.models.quran.CharType
import arg.quran.models.quran.VerseKey
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.quran.features.pager.uiState.PageItem
import org.quran.features.pager.uiState.QuranPage
import org.quran.features.pager.uiState.WordUiState

class QuranPageProvider : PreviewParameterProvider<QuranPage> {
  override val values: Sequence<QuranPage> = sequenceOf(
    QuranPage(
      page = 77,
      header = PageItem.Header(leading = "An-Nass", trailing = "juz'60"),
      lines = listOf(
        QuranPage.ChapterLine(1, 112),
        QuranPage.Basmallah(2),
        QuranPage.TextLine(
          line = 3,
          words = persistentListOf(
            WordUiState(6222, 1, VerseKey(112, 1), CharType.Word, 2, "ﭑ"),
            WordUiState(6222, 2, VerseKey(112, 1), CharType.Word, 3, "ﭒ"),
            WordUiState(6222, 3, VerseKey(112, 1), CharType.Word, 3, "ﭓ"),
            WordUiState(6222, 4, VerseKey(112, 1), CharType.Word, 3, "ﭔ"),
            WordUiState(6222, 5, VerseKey(112, 1), CharType.End, 3, "ﭕ"),
            WordUiState(6222, 1, VerseKey(112, 2), CharType.Word, 3, "ﭖ"),
            WordUiState(6222, 2, VerseKey(112, 2), CharType.Word, 3, "ﭗ"),
            WordUiState(6222, 3, VerseKey(112, 2), CharType.End, 3, "ﭘ"),
            WordUiState(6222, 1, VerseKey(112, 3), CharType.Word, 3, "ﭙ"),
            WordUiState(6222, 2, VerseKey(112, 3), CharType.Word, 3, "ﭚ"),
          )
        ),
        QuranPage.TextLine(
          line = 4,
          words = persistentListOf(
            WordUiState(6222, 3, VerseKey(112, 3), CharType.Word, 4, "ﭛ"),
            WordUiState(6222, 4, VerseKey(112, 3), CharType.Word, 4, "ﭜ"),
            WordUiState(6222, 5, VerseKey(112, 3), CharType.End, 4, "ﭝ"),
            WordUiState(6222, 1, VerseKey(112, 4), CharType.Word, 4, "ﭞ"),
            WordUiState(6222, 2, VerseKey(112, 4), CharType.Word, 4, "ﭟ"),
            WordUiState(6222, 3, VerseKey(112, 4), CharType.Word, 4, "ﭠ"),
            WordUiState(6222, 4, VerseKey(112, 4), CharType.Word, 4, "ﭡ"),
            WordUiState(6222, 5, VerseKey(112, 4), CharType.Word, 4, "ﭢ"),
            WordUiState(6222, 6, VerseKey(112, 4), CharType.End, 4, "ﭣ"),
          )
        ),
        QuranPage.ChapterLine(5, 113),
        QuranPage.Basmallah(6),
        QuranPage.TextLine(
          line = 7,
          words = persistentListOf(
            WordUiState(6222, 1, VerseKey(113, 1), CharType.Word, 7, "ﭤ"),
            WordUiState(6222, 2, VerseKey(113, 1), CharType.Word, 7, "ﭥ"),
            WordUiState(6222, 3, VerseKey(113, 1), CharType.Word, 7, "ﭦ"),
            WordUiState(6222, 4, VerseKey(113, 1), CharType.Word, 7, "ﭧ"),
            WordUiState(6222, 5, VerseKey(113, 1), CharType.End, 7, "ﭨ"),
            WordUiState(6222, 1, VerseKey(113, 2), CharType.Word, 7, "ﭩ"),
            WordUiState(6222, 2, VerseKey(113, 2), CharType.Word, 7, "ﭪ"),
            WordUiState(6222, 3, VerseKey(113, 2), CharType.Word, 7, "ﭫ"),
            WordUiState(6222, 4, VerseKey(113, 2), CharType.Word, 7, "ﭬ"),
            WordUiState(6222, 5, VerseKey(113, 2), CharType.End, 7, "ﭭ"),
            WordUiState(6222, 1, VerseKey(113, 3), CharType.Word, 7, "ﭮ"),
          )
        ),
        QuranPage.TextLine(
          line = 8,
          words = persistentListOf(
            WordUiState(6222, 2, VerseKey(113, 3), CharType.Word, 8, "ﭯ"),
            WordUiState(6222, 3, VerseKey(113, 3), CharType.Word, 8, "ﭰ"),
            WordUiState(6222, 4, VerseKey(113, 3), CharType.Word, 8, "ﭱ"),
            WordUiState(6222, 5, VerseKey(113, 3), CharType.Word, 8, "ﭲ"),
            WordUiState(6222, 5, VerseKey(113, 3), CharType.End, 8, "ﭳ"),
            WordUiState(6222, 1, VerseKey(113, 4), CharType.Word, 8, "ﭴ"),
            WordUiState(6222, 2, VerseKey(113, 4), CharType.Word, 8, "ﭵ"),
            WordUiState(6222, 3, VerseKey(113, 4), CharType.Word, 8, "ﭶ"),
            WordUiState(6222, 4, VerseKey(113, 4), CharType.Word, 8, "ﭷ"),
          )
        ),
        QuranPage.TextLine(
          line = 9,
          words = persistentListOf(
            WordUiState(6222, 5, VerseKey(113, 4), CharType.Word, 9, "ﭸ"),
            WordUiState(6222, 6, VerseKey(113, 4), CharType.End, 9, "ﭹ"),
            WordUiState(6222, 1, VerseKey(113, 5), CharType.Word, 9, "ﭺ"),
            WordUiState(6222, 2, VerseKey(113, 5), CharType.Word, 9, "ﭻ"),
            WordUiState(6222, 3, VerseKey(113, 5), CharType.Word, 9, "ﭼ"),
            WordUiState(6222, 4, VerseKey(113, 5), CharType.Word, 9, "ﭽ"),
            WordUiState(6222, 5, VerseKey(113, 5), CharType.Word, 9, "ﭾ"),
            WordUiState(6222, 6, VerseKey(113, 5), CharType.Word, 9, "ﭿ"),
          )
        ),
        QuranPage.ChapterLine(10, 114),
        QuranPage.Basmallah(11),
        QuranPage.TextLine(
          line = 12,
          words = persistentListOf(
            WordUiState(6222, 1, VerseKey(114, 1), CharType.Word, 12, "ﮀ"),
            WordUiState(6222, 2, VerseKey(114, 1), CharType.Word, 12, "ﮁ"),
            WordUiState(6222, 3, VerseKey(114, 1), CharType.Word, 12, "ﮂ"),
            WordUiState(6222, 4, VerseKey(114, 1), CharType.Word, 12, "ﮃ"),
            WordUiState(6222, 5, VerseKey(114, 1), CharType.End, 12, "ﮄ"),
            WordUiState(6222, 1, VerseKey(114, 2), CharType.Word, 12, "ﮅ"),
            WordUiState(6222, 2, VerseKey(114, 2), CharType.Word, 12, "ﮆ"),
            WordUiState(6222, 3, VerseKey(114, 2), CharType.End, 12, "ﮇ"),
            WordUiState(6222, 1, VerseKey(114, 3), CharType.Word, 12, "ﮈ"),
          )
        ),
        QuranPage.TextLine(
          line = 13,
          words = persistentListOf(
            WordUiState(6222, 2, VerseKey(114, 3), CharType.Word, 13, "ﮉ"),
            WordUiState(6222, 3, VerseKey(114, 3), CharType.End, 13, "ﮊ"),
            WordUiState(6222, 1, VerseKey(114, 4), CharType.Word, 13, "ﮋ"),
            WordUiState(6222, 2, VerseKey(114, 4), CharType.Word, 13, "ﮌ"),
            WordUiState(6222, 3, VerseKey(114, 4), CharType.Word, 13, "ﮍ"),
            WordUiState(6222, 4, VerseKey(114, 4), CharType.Word, 13, "ﮎ"),
            WordUiState(6222, 5, VerseKey(114, 4), CharType.End, 13, "ﮏ"),
            WordUiState(6222, 1, VerseKey(114, 5), CharType.Word, 13, "ﮐ"),
          )
        ),
        QuranPage.TextLine(
          line = 14,
          words = persistentListOf(
            WordUiState(6222, 2, VerseKey(114, 5), CharType.Word, 14, "ﮑ"),
            WordUiState(6222, 3, VerseKey(114, 5), CharType.Word, 14, "ﮒ"),
            WordUiState(6222, 4, VerseKey(114, 5), CharType.Word, 14, "ﮓ"),
            WordUiState(6222, 5, VerseKey(114, 5), CharType.Word, 14, "ﮔ"),
            WordUiState(6222, 6, VerseKey(114, 5), CharType.End, 14, "ﮕ"),
          )
        ),
        QuranPage.TextLine(
          line = 15,
          words = persistentListOf(
            WordUiState(6222, 1, VerseKey(114, 6), CharType.End, 15, "ﮖ"),
            WordUiState(6222, 2, VerseKey(114, 6), CharType.End, 15, "ﮗ"),
            WordUiState(6222, 3, VerseKey(114, 6), CharType.End, 15, "ﮘ"),
            WordUiState(6222, 4, VerseKey(114, 6), CharType.End, 15, "ﮙ"),
          )
        ),
      ).toPersistentList()
    )
  )
}