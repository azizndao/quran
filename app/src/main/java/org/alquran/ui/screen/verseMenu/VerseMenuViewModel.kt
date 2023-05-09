package org.alquran.ui.screen.verseMenu

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.muslimapp.core.audio.PlaybackConnection
import org.quran.bookmarks.model.Bookmark
import org.quran.bookmarks.repository.BookmarkRepository

class VerseMenuViewModel(
  savedStateHandle: SavedStateHandle,
  private val playbackConnection: PlaybackConnection,
  private val bookmarkRepository: BookmarkRepository,
) : ViewModel() {

  private val args = verseMenuDestinationArgs(savedStateHandle)

  var uiStateFlow = bookmarkRepository.getBookmarkFlow(args.key).map {
    VerseMenuUiState(
      label = "",
      key = args.key,
      word = args.word,
      isBookmarked = it.isNotEmpty(),
      onEvent = ::onEvent
    )
  }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

  private fun onEvent(event: VerseMenuEvent): Unit = when (event) {
    VerseMenuEvent.Bookmark -> toggleBookmark()
    VerseMenuEvent.Copy -> TODO()
    VerseMenuEvent.Play -> playbackConnection.onPlaySurah(args.key)
    VerseMenuEvent.PlayWord -> TODO()
    VerseMenuEvent.Repeat -> playbackConnection.repeatAyah(args.key)
  }

  private fun toggleBookmark() {
    val isBookmark = uiStateFlow.value?.isBookmarked ?: return
    viewModelScope.launch(Dispatchers.IO) {
      if (isBookmark) {
        bookmarkRepository.removeBookmark(args.key)
      } else {
        bookmarkRepository.addBookmark(Bookmark(key = args.key, name = "Bookmarks"))
      }
    }
  }
}
