package org.quran.translation.workers

import android.content.Context
import androidx.work.*
import arg.quran.models.quran.VerseTranslation
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.quran.datastore.repositories.QuranPreferencesRepository
import org.quran.network.translation.TranslationApiService
import org.quran.translation.databases.QuranTranslationsDatabase.Companion.getTranslationDatabase
import java.util.*

internal class DownloadTranslationWorker(
  private val translationApiService: TranslationApiService,
  private val quranPreferences: QuranPreferencesRepository,
  context: Context,
  params: WorkerParameters
) : CoroutineWorker(context, params) {
  override suspend fun doWork(): Result {

    val slug = inputData.getString(PARAMS_SLUG)!!

    val translation = quranPreferences.getTranslationEdition(slug)

    val apiTranslations = translationApiService.getAyahTranslations(translation!!.id)

    applicationContext.assets.open("page_mapping.json").use {
      val metas: List<VerseMeta> = Json.decodeFromStream(it)
      val translations = metas.zip(apiTranslations) { meta, translation ->
        VerseTranslation(
          id = meta.id,
          key = translation.key,
          page = meta.v1Page,
          text = translation.text,
        )
      }
      applicationContext.getTranslationDatabase(slug).ayahDao.insertAyahs(translations)
    }
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

  @kotlinx.serialization.Serializable
  private data class VerseMeta(
    val id: Int,
    @SerialName("v1_page") val v1Page: Int,
    @SerialName("v2_page") val v2Page: Int
  )
}