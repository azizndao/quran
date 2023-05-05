package org.quran.translation.workers

import android.content.Context
import androidx.work.*
import kotlinx.serialization.SerialName
import org.quran.datastore.repositories.QuranPreferencesRepository
import org.quran.network.translation.TranslationApiService
import org.quran.translation.databases.QuranTranslationsDatabase.Companion.getTranslationDatabase
import org.quran.translation.models.TranslatedVerse
import java.util.*

internal class DownloadTranslationWorker(
  private val translationApiService: TranslationApiService,
  private val quranPreferences: QuranPreferencesRepository,
  context: Context,
  params: WorkerParameters
) : CoroutineWorker(context, params) {
  override suspend fun doWork(): Result {

    val slug = inputData.getString(PARAMS_SLUG)!!

    val translation = quranPreferences.getLocaleTranslation(slug)

    val apiTranslations = translationApiService.getVerses(translation!!.id).map { verse ->
      TranslatedVerse(
        sura = verse.sura,
        ayah = verse.ayah,
        text = verse.text
      )
    }

    applicationContext.getTranslationDatabase(slug).verses.insertAyahs(apiTranslations)
    return Result.failure()
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