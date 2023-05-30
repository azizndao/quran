package org.quran.features.pager.uiState

import arg.quran.models.audio.Qari
import arg.quran.models.quran.VerseKey
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.flow.StateFlow
import org.quran.bookmarks.models.BookmarkTag

sealed class DialogUiState {

  object None : DialogUiState()

  data class VerseMenu(
    val verse: VerseKey,
    val bookmarked: Boolean,
  ) : DialogUiState()

  data class SelectBookmarkTab(
    val verse: VerseKey,
    val tags: PersistentList<BookmarkTag>,
  ) : DialogUiState()

  data class ShowBookmarks(
    val verse: VerseKey,
    val tags: PersistentList<BookmarkTag>,
  ) : DialogUiState()


  data class AudioMenu(
    val qaris: PersistentList<Qari>,
    val nowPlaying: StateFlow<AudioUiState?>
  ) : DialogUiState()
}