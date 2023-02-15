package org.quran.translation.databases

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import io.ktor.util.reflect.*
import org.quran.translation.dao.VerseTranslationDao
import org.quran.translation.databases.converstors.VerseKeyConvertor
import org.quran.translation.model.VerseTranslation
import org.quran.translation.workers.DownloadTranslationWorker

@Database(
    version = 1,
    exportSchema = true,
    entities = [VerseTranslation::class],
)
@TypeConverters(VerseKeyConvertor::class)
internal abstract class QuranTranslationsDatabase : RoomDatabase() {

    abstract val ayahDao: VerseTranslationDao

    companion object {
        @Volatile
        private var instances = mutableMapOf<String, QuranTranslationsDatabase>()

        fun Context.getTranslationDatabase(slug: String): QuranTranslationsDatabase {
            return synchronized(this) {
                if (instances[slug] == null) {
                    instances[slug] = Room
                        .databaseBuilder(this, QuranTranslationsDatabase::class.java, slug)
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                DownloadTranslationWorker.enqueue(this@getTranslationDatabase, slug)
                            }
                        })
                        .build()
                }
                instances[slug]!!
            }
        }
    }
}
