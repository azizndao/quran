package org.quran.features.pager.useCase


import arg.quran.models.Sura
import arg.quran.models.quran.VerseKey
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.alquran.verses.repository.VerseRepository
import org.quram.common.repositories.SurahRepository
import org.quram.common.utils.QuranDisplayData
import org.quran.bookmarks.repository.BookmarkRepository
import org.quran.core.audio.PlaybackConnection
import org.quran.features.pager.uiState.PageItem
import org.quran.features.pager.uiState.QuranPage
import org.quran.features.pager.uiState.toUiState

class GetQuranPageUseCase(
  private val verseRepository: VerseRepository,
  private val bookmarkRepository: BookmarkRepository,
  private val surahRepository: SurahRepository,
  private val pageHeaderUseCase: PageHeaderUseCase,
  private val quranDisplayData: QuranDisplayData,
  private val playbackConnection: PlaybackConnection,
) {

  @Suppress("UNUSED_PARAMETER")
  operator fun invoke(
    page: Int,
    version: Int,
    selectionFlow: Flow<VerseKey?>
  ): Flow<QuranPage> {

    var pageHeader: PageItem.Header? = null

    var suras: List<Sura>? = null

    val keys = quranDisplayData.getAyahKeysOnPage(page)

    return combine(
      verseRepository.getVersesWordsByPage(page),
      bookmarkRepository.observeBookmarksWithKeys(keys),
      playbackConnection.currentAyah,
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
              add(QuranPage.ChapterLine(size + 1, sura))
            }
            if (sura != 1 && sura != 9 && firstWord.line >= 2) {
              add(QuranPage.Basmallah(size + 1))
            }
          }

          add(
            QuranPage.TextLine(
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
          val lastLine = last() as QuranPage.TextLine
          val line = QuranPage.ChapterLine(size + 1, lastLine.words[0].key.sura + 1)
          add(line)
        }
      }

      QuranPage(
        header = pageHeader!!,
        page = page,
        lines = data.toPersistentList(),
      )
    }
  }
}