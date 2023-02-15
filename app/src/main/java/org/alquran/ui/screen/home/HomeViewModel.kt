package org.alquran.ui.screen.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import org.alquran.ui.screen.home.bookmarks.BookmarkUiModel
import org.alquran.ui.screen.home.bookmarks.ListBookmarksUiState
import org.alquran.ui.screen.home.juzs.JuzListUiState
import org.alquran.usecases.GetListOfHizbUseCase
import org.alquran.usecases.ListSurahUseCase
import org.muslimapp.domain.quran.uistate.SurahListUiState
import org.quram.common.utils.QuranInfo
import org.quran.bookmarks.repository.BookmarkRepository

internal class HomeViewModel(
    listSurahUseCase: ListSurahUseCase,
    getListOfHizb: GetListOfHizbUseCase,
    bookmarkRepository: BookmarkRepository,
    private val quranInfo: QuranInfo,
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
                    page = quranInfo.getPageFromSuraAyah(it.key.sura, it.key.ayah),
                    nameSimple = it.name
                )
            }
            ListBookmarksUiState(loading = false, bookmarks = bookmarks)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ListBookmarksUiState())
}