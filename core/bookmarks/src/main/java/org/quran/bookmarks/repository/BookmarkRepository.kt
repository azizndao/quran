package org.quran.bookmarks.repository

import kotlinx.coroutines.flow.distinctUntilChanged
import org.quram.common.model.VerseKey
import org.quran.bookmarks.databases.BookmarksDatabase
import org.quran.bookmarks.model.Bookmark

class BookmarkRepository internal constructor(db: BookmarksDatabase) {
    private val dao = db.bookmarkDao

    suspend fun addBookmark(bookmark: Bookmark) = dao.insertBookmarks(bookmark)

    suspend fun removeBookmark(key: VerseKey) = dao.delete(key)

    suspend fun removeBookmark(bookmark: Bookmark) = dao.delete(bookmark)

    suspend fun getBookmark(key: VerseKey): Bookmark? = dao.getBookmark(key)

    fun getAllBookmarks() = dao.getAll().distinctUntilChanged()

    fun getBookmarksWithKeys(keys: Set<VerseKey>) = dao.getByPage(keys).distinctUntilChanged()
}