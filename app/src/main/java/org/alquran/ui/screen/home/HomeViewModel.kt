package org.alquran.ui.screen.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.alquran.ui.screen.home.bookmarks.BookmarkUiModel
import org.alquran.ui.screen.home.bookmarks.ListBookmarksUiState
import org.alquran.ui.screen.home.juzs.JuzListUiState
import org.alquran.ui.uistate.SurahListUiState
import org.alquran.usecases.GetListOfHizbUseCase
import org.alquran.usecases.ListSurahUseCase
import org.alquran.verses.repository.VerseRepository
import org.quram.common.core.QuranInfo
import org.quran.bookmarks.repository.BookmarkRepository
import org.quran.domain.quran.workers.SetupDatabaseWorker

internal class HomeViewModel(
  listSurahUseCase: ListSurahUseCase,
  getListOfHizb: GetListOfHizbUseCase,
  bookmarkRepository: BookmarkRepository,
  private val quranInfo: QuranInfo,
  private val verseRepository: VerseRepository,
  app: Application,
) : AndroidViewModel(app) {

  val surahsFlow: StateFlow<SurahListUiState> = listSurahUseCase.invoke(viewModelScope)

  val hibzUiStateFlow = MutableStateFlow(JuzListUiState(data = getListOfHizb(), loading = false))

  val bookmarksUiStateFlow: StateFlow<ListBookmarksUiState> = bookmarkRepository.getAllBookmarks()
    .map { items ->
      val bookmarks = items.map {
        BookmarkUiModel(
          key = it.key,
          tag = it.tag,
          page = quranInfo.getPageFromSuraAyah(it.key.sura, it.key.aya),
          nameSimple = it.name
        )
      }
      ListBookmarksUiState(loading = false, bookmarks = bookmarks)
    }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ListBookmarksUiState())


  init {
    viewModelScope.launch {
      if (verseRepository.hasMissingVerses()) {
        verseRepository.setupDatabase()
      }
    }
  }
}