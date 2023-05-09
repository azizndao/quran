package org.quran.translation.workers

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Operation
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.quran.datastore.repositories.QuranPreferencesRepository
import org.quran.translation.api.TranslationApiService
import org.quran.translation.local.QuranTranslationsDatabase.Companion.getTranslationDatabase
import org.quran.translation.local.models.TranslatedVerse
import org.quran.translation.repositories.TranslationRepository
import timber.log.Timber
import java.io.IOException

class DownloadTranslationWorker(
  context: Context,
  params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {
  private val apiService: TranslationApiService by inject()
  private val quranPreferences: QuranPreferencesRepository by inject()
  private val translationRepository: TranslationRepository by inject()

  override suspend fun doWork() = try {
    val slug = inputData.getString(PARAMS_SLUG)!!

    val translation = translationRepository.getTranslation(slug)

    val response = apiService.getVerses(translation!!.id)
    val apiTranslations = response.translations.map { verse ->
      TranslatedVerse(
        sura = verse.sura,
        ayah = verse.ayah,
        text = verse.text
      )
    }

    applicationContext.getTranslationDatabase(slug).verses.insertAyahs(apiTranslations)
    quranPreferences.downloadTranslation(translation.slug)
    Result.success()
  } catch (e: IOException) {
    Timber.e(e)
    Result.retry()
  } catch (e: Exception) {
    Timber.e(e)
    Result.failure(workDataOf("error" to e))
  }


  companion object {
    const val PARAMS_SLUG = "slug"

    fun enqueue(context: Context, slug: String): Operation {
      val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

      val request = OneTimeWorkRequestBuilder<DownloadTranslationWorker>()
        .setConstraints(constraints)
        .setInputData(workDataOf(PARAMS_SLUG to slug))
        .build()

      return WorkManager.getInstance(context)
        .enqueueUniqueWork(slug, ExistingWorkPolicy.KEEP, request)
    }
  }
}