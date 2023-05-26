package org.quran.bookmarks.repository

import arg.quran.models.quran.VerseKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import org.quran.bookmarks.databases.BookmarksDatabase
import org.quran.bookmarks.models.Bookmark
import org.quran.bookmarks.models.BookmarkTag
import org.quran.bookmarks.models.TabWithBookmarks

class BookmarkRepository internal constructor(db: BookmarksDatabase) {
  private val bookmarkDao = db.bookmarkDao
  private val tagDao = db.tagDao

  fun observeAllTags(): Flow<List<BookmarkTag>> = tagDao.observeAll()

  suspend fun getAllTags(): List<BookmarkTag> = tagDao.getAll()

  suspend fun addTag(name: String, color: Int? = null) =
    tagDao.insertOne(BookmarkTag(name = name, color = color))

  suspend fun addReturnTag(name: String, color: Int? = null) =
    tagDao.insertAndReturn(BookmarkTag(name = name, color = color))

  suspend fun addAll(tags: List<BookmarkTag>) = tagDao.insertMany(tags)

  fun observeBookmarkByKey(key: VerseKey): Flow<List<Bookmark>> = bookmarkDao.observeBookmark(key)

  suspend fun getBookmarkByKey(key: VerseKey): List<Bookmark> = bookmarkDao.getBookmark(key)

  suspend fun addBookmark(bookmark: Bookmark) = bookmarkDao.insertBookmarks(bookmark)

  suspend fun removeBookmark(key: VerseKey) = bookmarkDao.delete(key)

  fun observeBookmarksWithKeys(keys: Set<VerseKey>) =
    bookmarkDao.getByPage(keys).distinctUntilChanged()

  suspend fun getAllTagfsForVerse(verse: VerseKey) = bookmarkDao.getAllTagsForVerse(verse)

  fun observeTabWithBookmarks(): Flow<List<TabWithBookmarks>> {
    return tagDao.observeTabWithBookmarks()
  }
}