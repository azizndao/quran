package org.quran.translation.workers

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Operation
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.quran.network.workers.base.LongRunningDownloadWorker
import org.quran.translation.repositories.TranslationsRepository
import org.quran.translation.repositories.VerseTranslationRepository
import timber.log.Timber
import java.io.IOException
import java.util.Calendar

class DownloadTranslationWorker(
  context: Context,
  params: WorkerParameters,
) : LongRunningDownloadWorker(context, params), KoinComponent {

  private val translationRepository: TranslationsRepository by inject()
  private val verseRepository: VerseTranslationRepository by inject()

  private val notificationId = Calendar.getInstance().get(Calendar.MILLISECOND)

  override suspend fun getForegroundInfo(): ForegroundInfo {
    return ForegroundInfo(notificationId, notificationBuilder.build())
  }

  override suspend fun doWork(): Result {
    val slug = inputData.getString(PARAM_SLUG) ?: return Result.failure()
    val translation = translationRepository.getTranslation(slug) ?: return Result.failure()

    val notification = notificationBuilder
      .setContentTitle("Translation: ${translation.authorName}")
      .build()

    setForeground(ForegroundInfo(notificationId, notification))

    return try {
      verseRepository.downloadTranslations(translation)
      Result.success()
    } catch (e: IOException) {
      Timber.e(e)
      Result.retry()
    } catch (e: Exception) {
      Timber.e(e)
      Result.failure(workDataOf("error" to e))
    }
  }

  companion object {

    const val PARAM_SLUG = "slug"

    fun enqueue(context: Context, slug: String): Operation {
      val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

      val request = OneTimeWorkRequestBuilder<DownloadTranslationWorker>()
        .setExpedited(OutOfQuotaPolicy.DROP_WORK_REQUEST)
        .setInputData(workDataOf(PARAM_SLUG to slug))
        .setConstraints(constraints)
        .build()

      WorkManager.getInstance(context)
        .enqueueUniqueWork(slug, ExistingWorkPolicy.REPLACE, request)

      return WorkManager.getInstance(context)
        .enqueueUniqueWork(slug, ExistingWorkPolicy.REPLACE, request)
    }
  }
}