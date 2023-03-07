package org.quran.domain.quran.workers

import android.content.Context
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import org.alquran.hafs.repository.VerseRepository
import org.quran.datastore.serializers.DEFAULT_QURAN_FONT_VERSION
import org.quran.domain.quran.R
import org.quran.network.verse.VerseApiService

class DownloadVersesWorker(
  private val verseRepository: VerseRepository,
  private val apiService: VerseApiService,
  context: Context,
  params: WorkerParameters
) : DownloadWorker(context, params) {
  override suspend fun doWork(): Result = try {

    val version = inputData.getInt(PARAM_VERSION, DEFAULT_QURAN_FONT_VERSION)

    for (juz in 1..30) {
      val verseList = apiService.downloadVerseByJuz(juz, version)

      verseRepository.insertVerses(verseList)

      setForeground(
        ForegroundInfo(
          NOTIFICATION_ID,
          notificationBuilder
            .setContentText(applicationContext.getString(R.string.downloading_quran_verses))
            .setProgress(30, juz, false).build()
        )
      )
    }
    Result.success()
  } catch (e: Exception) {
    Result.retry()
  }

  override suspend fun getForegroundInfo(): ForegroundInfo {
    return ForegroundInfo(
      NOTIFICATION_ID, notificationBuilder
        .setContentText(applicationContext.getString(R.string.downloading_quran_verses))
        .build()
    )
  }

  companion object {
    const val NOTIFICATION_ID = 12121
    const val PARAM_VERSION = "version"
  }
}