package org.alquran.ui.screen.home.bookmarks

import org.quram.common.model.VerseKey
import org.quran.bookmarks.model.BookmarkTag
import java.util.*

data class ListBookmarksUiState(
    val loading: Boolean = true,
    val bookmarks: List<BookmarkUiModel> = emptyList(),
    val exception: Exception? = null
)

data class BookmarkUiModel(
    val nameSimple: String,
    val key: VerseKey,
    val page: Int,
    val tag: BookmarkTag,
    val createdAt: Calendar = Calendar.getInstance(),
)
