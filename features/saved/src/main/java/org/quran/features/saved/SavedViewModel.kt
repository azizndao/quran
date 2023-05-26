package org.quran.features.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.quran.bookmarks.repository.BookmarkRepository

internal class SavedViewModel(
  bookmarkRepository: BookmarkRepository
) : ViewModel() {

  val uiStateFlow = bookmarkRepository.observeTabWithBookmarks().map { tags ->
    SavedUiState(bookmarksTags = tags.toPersistentList())
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SavedUiState(persistentListOf()))
}