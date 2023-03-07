package org.alquran.hafs.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import arg.quran.models.quran.Verse
import org.alquran.hafs.convertors.WordListConvertor
import org.alquran.hafs.dao.VerseDao

@Database(
  version = 1,
  entities = [Verse::class]
)
@TypeConverters(WordListConvertor::class)
abstract class VersesDatabase : RoomDatabase() {

  abstract val dao: VerseDao
}
