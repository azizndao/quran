package org.quran.translation.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import org.quran.translation.local.dao.QuranTranslationDao
import org.quran.translation.local.model.TranslatedVerse
import org.quran.translation.workers.DownloadTranslationWorker

@Database(
  version = 1,
  exportSchema = true,
  entities = [TranslatedVerse::class],
  autoMigrations = []
)
internal abstract class QuranTranslationsDatabase : RoomDatabase() {

  abstract val ayahDao: QuranTranslationDao

  companion object {

    private val openedInstances: MutableMap<String, QuranTranslationsDatabase> = mutableMapOf()

    fun Context.getTranslationDatabase(slug: String): QuranTranslationsDatabase {
      return synchronized(this) {
        if (!openedInstances.containsKey(slug)) {
          val callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
              DownloadTranslationWorker.enqueue(this@getTranslationDatabase, slug)
            }
          }
          openedInstances[slug] = Room
            .databaseBuilder(this, QuranTranslationsDatabase::class.java, slug)
            .addCallback(callback)
            .build()
        }
        openedInstances[slug]!!
      }
    }
  }
}

