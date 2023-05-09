package org.alquran.verses.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import arg.quran.models.quran.QuranWord
import arg.quran.models.quran.VerseKeyConvertor
import org.alquran.verses.convertors.WordListConvertor
import org.alquran.verses.local.dao.SearchableVerseDao
import org.alquran.verses.local.dao.VerseDao
import org.alquran.verses.local.dao.VerseWordDao
import org.alquran.verses.local.models.QuranicVerse
import org.alquran.verses.local.models.SearchableVerse

@Database(
  version = 1,
  entities = [QuranicVerse::class, SearchableVerse::class, QuranWord::class]
)
@TypeConverters(WordListConvertor::class, VerseKeyConvertor::class)
internal abstract class VersesDatabase : RoomDatabase() {

  abstract val verses: VerseDao
  abstract val words: VerseWordDao
  abstract val searchables: SearchableVerseDao
}
