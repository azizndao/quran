package org.quran.bookmarks.repository

import arg.quran.models.quran.VerseKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import org.quran.bookmarks.databases.BookmarksDatabase
import org.quran.bookmarks.model.Bookmark

class BookmarkRepository internal constructor(db: BookmarksDatabase) {
  private val dao = db.bookmarkDao

  fun getBookmarkFlow(key: VerseKey): Flow<List<Bookmark>> = dao.getBookmark(key)

  suspend fun addBookmark(bookmark: Bookmark) = dao.insertBookmarks(bookmark)

  suspend fun removeBookmark(key: VerseKey) = dao.delete(key)

  fun getAllBookmarks() = dao.getAll().distinctUntilChanged()

  fun getBookmarksWithKeys(keys: Set<VerseKey>) = dao.getByPage(keys).distinctUntilChanged()
}