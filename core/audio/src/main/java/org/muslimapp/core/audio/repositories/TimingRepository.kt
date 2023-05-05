package org.muslimapp.core.audio.repositories

import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.util.SparseIntArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.muslimapp.core.audio.databases.SuraTimingDatabaseHandler
import org.muslimapp.core.audio.databases.SuraTimingDatabaseHandler.Companion.getDatabaseHandler
import org.quram.common.source.MadaniPageProvider
import org.quran.network.audio.AudioApiService
import timber.log.Timber
import java.io.File

class TimingRepository(
  private val context: Context,
  private val audioApiService: AudioApiService,
  private val pageProvider: MadaniPageProvider,
) {

  suspend fun getPosition(reciter: String, sura: Int, ayah: Int): Long {
    return getOrDownloadDatabaseHandler(reciter).getAyahTiming(sura, ayah)?.use { cursor ->
      if (cursor.moveToFirst()) {
        cursor.getLong(2)
      } else {
        null
      }
    } ?: 0L
  }

  private suspend fun getOrDownloadDatabaseHandler(slug: String): SuraTimingDatabaseHandler {
    val databaseBaseUrl = pageProvider.getDatabaseDirectoryName()
    val dbFile = File("$databaseBaseUrl/$slug.db")
    if (!dbFile.exists()) audioApiService.getTimingsDatabase(slug)
    return getDatabaseHandler(dbFile.path)
  }

  suspend fun getGaplessData(slug: String, sura: Int) = withContext(Dispatchers.IO) {
    val db = getOrDownloadDatabaseHandler(slug)

    val map = SparseIntArray()

    var cursor: Cursor? = null
    try {
      cursor = db.getAyahTimings(sura)
      Timber.d("got cursor of data")
      if (cursor != null && cursor.moveToFirst()) {
        do {
          val ayah = cursor.getInt(1)
          val time = cursor.getInt(2)
          map.put(ayah, time)
        } while (cursor.moveToNext())
      }
    } catch (se: SQLException) {
      // don't crash the app if the database is corrupt
      Timber.e(se)
      context.deleteDatabase(slug)
    } finally {
      cursor?.close()
    }

    map
  }
}