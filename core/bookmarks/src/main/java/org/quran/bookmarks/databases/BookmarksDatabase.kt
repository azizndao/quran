package org.quran.bookmarks.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.quran.bookmarks.dao.BookmarkDao
import org.quran.bookmarks.databases.convertors.BookmarkTagConvertor
import org.quran.bookmarks.databases.convertors.CalendarConvertor
import org.quran.bookmarks.databases.convertors.VerseKeyConvertor
import org.quran.bookmarks.model.Bookmark

@Database(
  version = 1,
  entities = [Bookmark::class]
)
@TypeConverters(BookmarkTagConvertor::class, CalendarConvertor::class, VerseKeyConvertor::class)
internal abstract class BookmarksDatabase : RoomDatabase() {

  abstract val bookmarkDao: BookmarkDao
}