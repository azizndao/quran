package org.alquran.verses.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import org.alquran.verses.repository.VerseRepository
import timber.log.Timber

class SetupDatabaseWorker(
  context: Context,
  params: WorkerParameters,
  private val verseRepository: VerseRepository,
) : CoroutineWorker(context, params) {

  override suspend fun doWork(): Result {
    return try {
      verseRepository.setupDatabase()
      Result.success()
    } catch (e: Exception) {
      Timber.e(e)
      Result.failure()
    }
  }

  companion object {
    fun enqueue(context: Context) {
      val work = OneTimeWorkRequestBuilder<SetupDatabaseWorker>()
        .build()

      WorkManager.getInstance(context)
        .enqueueUniqueWork("setupDatabaseWorker", ExistingWorkPolicy.KEEP, work)
    }
  }
}