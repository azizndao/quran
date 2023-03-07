package org.hadeeths

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.hadeeths.dao.HadithBookmarkDao
import org.hadeeths.dao.HadithDao
import org.hadeeths.models.*

@Database(
  version = 1,
  exportSchema = true,
  entities = [
    HadithCategory::class,
    HadithSummaryEntity::class,
    Hadith::class,
    TranslatedHadith::class,
    HadithLanguage::class,
    HadithBookmark::class,
  ],
  autoMigrations = [
//        AutoMigration(from = 1, to = 2),
//        AutoMigration(from = 2, to = 3, spec = DatabaseMigrations.Migration2To3::class),
//        AutoMigration(from = 3, to = 4, spec = DatabaseMigrations.Migration3To4::class),
//        AutoMigration(from = 4, to = 5, spec = DatabaseMigrations.Migration4To5::class),
  ]
)
@TypeConverters(
  ListStringConvertors::class,
  WordsMeaningConvertors::class,
  CalendarConvertor::class
)
internal abstract class HadithsDatabase : RoomDatabase() {

  abstract val hadithDao: HadithDao

  abstract val bookmarkDao: HadithBookmarkDao
}