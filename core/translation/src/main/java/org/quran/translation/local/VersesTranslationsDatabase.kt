package org.quran.translation.local

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import org.quran.translation.local.dao.VerseTranslationDao
import org.quran.translation.local.models.TranslatedVerse
import org.quran.translation.workers.DownloadTranslationWorker

@Database(
  version = 1,
  exportSchema = true,
  entities = [TranslatedVerse::class],
)
internal abstract class QuranTranslationsDatabase : RoomDatabase() {

  abstract val verses: VerseTranslationDao

  companion object {
    @Volatile
    private var instances = mutableMapOf<String, QuranTranslationsDatabase>()

    fun Context.getTranslationDatabase(slug: String) = synchronized(this) {
      if (instances[slug] == null) {
        instances[slug] = Room
          .databaseBuilder(this, QuranTranslationsDatabase::class.java, slug)
          .addCallback(DatabaseCallback(this, slug))
          .build()
      }
      instances[slug]!!
    }
  }

  private class DatabaseCallback(
    private val context: Context,
    private val slug: String,
  ) : Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
      DownloadTranslationWorker.enqueue(context, slug)
    }
  }
}

