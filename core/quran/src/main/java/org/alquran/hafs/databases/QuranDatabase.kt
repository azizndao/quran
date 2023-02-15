package org.alquran.hafs.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.alquran.hafs.convertors.VerseKeyConvertor
import org.alquran.hafs.convertors.WordListConvertor
import org.alquran.hafs.dao.VerseDao
import org.alquran.hafs.model.Verse
import org.alquran.hafs.model.VerseTranslation

@Database(
    version = 1,
    entities = [Verse::class, VerseTranslation::class]
)
@TypeConverters(
    VerseKeyConvertor::class,
    WordListConvertor::class,
)
abstract class QuranDatabase : RoomDatabase() {

    abstract val dao: VerseDao

    companion object {

        @Volatile
        private var instance: QuranDatabase? = null

        @Volatile
        private var currentVersion: Int? = null

        fun getInstance(context: Context, version: Int): QuranDatabase = synchronized(this) {
            if (version != currentVersion) {
                instance?.close()

                currentVersion = version

                instance = Room
                    .databaseBuilder(context, QuranDatabase::class.java, "quran.v$version.db")
                    .build()
            }
            instance!!
        }
    }
}
