package org.quran.features.saved

import kotlinx.collections.immutable.PersistentList
import org.quran.bookmarks.models.TabWithBookmarks

data class SavedUiState(
  val bookmarksTags: PersistentList<TabWithBookmarks>
)
