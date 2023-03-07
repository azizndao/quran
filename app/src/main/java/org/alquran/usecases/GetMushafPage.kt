package org.alquran.usecases


import android.content.Context
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import arg.quran.models.Sura
import arg.quran.models.quran.VerseWord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.alquran.R
import org.alquran.hafs.repository.VerseRepository
import org.alquran.ui.uistate.MushafPage
import org.alquran.ui.uistate.QuranPageItem
import org.alquran.ui.uistate.toUiState
import org.quram.common.repositories.SurahRepository
import org.quram.common.utils.QuranDisplayData
import org.quram.common.utils.UriProvider
import org.quran.bookmarks.repository.BookmarkRepository

class GetMushafPage(
  private val verseRepository: VerseRepository,
  private val bookmarkRepository: BookmarkRepository,
  private val surahRepository: SurahRepository,
  private val pageHeaderUseCase: PageHeaderUseCase,
  private val context: Context,
  private val quranDisplayData: QuranDisplayData,
) {

  operator fun invoke(
    page: Int,
    version: Int,
  ): Flow<MushafPage> {

    var pageHeader: QuranPageItem.Header? = null

    var suras: List<Sura>? = null

    val keys = quranDisplayData.getAyahKeysOnPage(page)

    return combine(
      verseRepository.getVerses(page, version),
      bookmarkRepository.getBookmarksWithKeys(keys),
    ) { verses, bookmarks ->
      if (suras == null) {
        suras = surahRepository.getSurahsInPage(page)
      }

      if (pageHeader == null) {
        pageHeader = pageHeaderUseCase(page)
      }

      val data: MutableList<MushafPage.Line>

      val lineMap =
        verses.fold(mutableListOf<VerseWord>()) { words, verse ->
          words.addAll(verse.words)
          words
        }.groupBy { it.line }

      data = lineMap.entries.fold(mutableListOf()) { lines, (lineNumber, words) ->

        val firstWord = words.first()
        val (sura, aya) = firstWord.key

        if (firstWord.position == 1 && aya == 1) {
          if (firstWord.line >= 3 || firstWord.key.sura == 1) {
            lines.add(MushafPage.ChapterLine(lines.size + 1, sura))
          }
          if (sura != 1 && sura != 9 && firstWord.line >= 2) {
            lines.add(MushafPage.Basmallah(lines.size + 1))
          }
        }

        lines.add(
          MushafPage.TextLine(
            line = lineNumber,
            words = words.map {
              it.toUiState(
                playing = false,
                selected = false,
              )
            },
          ),
        )

        lines
      }

      if (data.size == 14) {
        val lastLine = data.last()
        if (lastLine is MushafPage.TextLine) {
          data.add(
            MushafPage.ChapterLine(
              data.size + 1,
              lastLine.words.last().key.sura + 1
            )
          )
        }
      }

      MushafPage(
        header = pageHeader!!,
        page = page,
        lines = data,
        fontFamily = FontFamily(
          if (page == 46) Font(R.font.p46) else Font(
            UriProvider.getFontFile(context, page, version)
          )
        )
      )
    }
  }
}