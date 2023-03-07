package org.quran.domain.quran.workers

import android.content.Context
import androidx.work.*
import kotlinx.coroutines.flow.first
import org.alquran.hafs.repository.VerseRepository
import org.quran.datastore.repositories.QuranPreferencesRepository
import timber.log.Timber

class SyncWorker(
  context: Context,
  params: WorkerParameters,
  private val verseRepository: VerseRepository,
  private val quranPreferences: QuranPreferencesRepository
) : CoroutineWorker(context, params) {
  override suspend fun doWork(): Result {
    try {
      val workManager = WorkManager.getInstance(applicationContext)

      val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

      val version = quranPreferences.getFontVersion().first()

      if (verseRepository.hasMissingVerses()) {
        val downloadVersesWork = OneTimeWorkRequestBuilder<DownloadVersesWorker>()
          .setConstraints(constraints)
          .build()
        workManager.enqueueUniqueWork(
          "DownloadVersesWorker",
          ExistingWorkPolicy.KEEP,
          downloadVersesWork
        )
      }

//            val downloadFontsWork = OneTimeWorkRequestBuilder<DownloadFontsWorker>()
//                .setConstraints(constraints)
//                .build()
//            workManager.enqueueUniqueWork(
//                "DownloadFontsWorker",
//                ExistingWorkPolicy.KEEP,
//                downloadFontsWork
//            )
      return Result.success()
    } catch (e: Exception) {
      Timber.e(e)
      return Result.failure(workDataOf("error" to e))
    }
  }
}