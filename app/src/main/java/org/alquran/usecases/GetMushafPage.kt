package org.alquran.usecases


import android.content.Context
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import arg.quran.models.Sura
import arg.quran.models.quran.VerseKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.alquran.ui.uistate.MushafPage
import org.alquran.ui.uistate.QuranPageItem
import org.alquran.ui.uistate.QuranSelection
import org.alquran.ui.uistate.toUiState
import org.alquran.verses.repository.VerseRepository
import org.muslimapp.core.audio.PlaybackConnection
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
  private val playbackConnection: PlaybackConnection,
) {

  operator fun invoke(
    page: Int,
    version: Int,
    selection: MutableStateFlow<QuranSelection>
  ): Flow<MushafPage> {

    var pageHeader: QuranPageItem.Header? = null

    var suras: List<Sura>? = null

    val keys = quranDisplayData.getAyahKeysOnPage(page)

    val selectionFlow = selection.map {
      when (it) {
        is QuranSelection.InitialVerse -> it.key
        is QuranSelection.Highlight -> it.key
        else -> null
      }
    }

    return combine(
      verseRepository.getVersesWordsByPage(page),
      bookmarkRepository.getBookmarksWithKeys(keys),
      playbackConnection.playingAyahFlow,
      selectionFlow
    ) { pageWords, bookmarks, verseKey, selectedAya ->

      if (suras == null) suras = surahRepository.getSurahsInPage(page)

      if (pageHeader == null) pageHeader = pageHeaderUseCase(page)

      val data = buildList {
        val listMap = pageWords.groupBy { it.line }.toSortedMap()
        val bookmarkMap = mutableMapOf<VerseKey, Boolean>()
        listMap.forEach { (lineNumber, lineWords) ->
          val words = lineWords.sortedBy { it.key }
          val firstWord = words.first()
          val (sura, aya) = firstWord.key

          if (firstWord.position == 1 && aya == 1) {
            if (firstWord.line >= 3 || firstWord.key.sura == 1 || firstWord.key.sura == 9) {
              add(MushafPage.ChapterLine(size + 1, sura))
            }
            if (sura != 1 && sura != 9 && firstWord.line >= 2) {
              add(MushafPage.Basmallah(size + 1))
            }
          }

          add(
            MushafPage.TextLine(
              line = lineNumber,
              words = words.map {

                if (it.key !in bookmarkMap) {
                  bookmarkMap[it.key] = bookmarks.any { bookmark -> it.key == bookmark.key }
                }

                it.toUiState(
                  playing = it.key == verseKey,
                  selected = selectedAya == it.key,
                  bookmarked = bookmarkMap[it.key]!!,
                )
              },
            ),
          )
        }

        if (size == 14) {
          val lastLine = last() as MushafPage.TextLine
          val line = MushafPage.ChapterLine(size + 1, lastLine.words[0].key.sura + 1)
          add(line)
        }
      }

      MushafPage(
        header = pageHeader!!,
        page = page,
        lines = data,
        fontFamily = FontFamily(Font(UriProvider.getFontFile(context, page, version)))
      )
    }
  }
}