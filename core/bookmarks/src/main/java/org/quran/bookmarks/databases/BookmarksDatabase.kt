package org.quran.bookmarks.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.quran.bookmarks.dao.BookmarkDao
import org.quran.bookmarks.dao.BookmarkTagDao
import org.quran.bookmarks.databases.convertors.VerseKeyConvertor
import org.quran.bookmarks.models.Bookmark
import org.quran.bookmarks.models.BookmarkTag

@Database(
  version = 1,
  entities = [Bookmark::class, BookmarkTag::class]
)
@TypeConverters(VerseKeyConvertor::class)
internal abstract class BookmarksDatabase : RoomDatabase() {

  abstract val bookmarkDao: BookmarkDao

  abstract val tagDao: BookmarkTagDao
}