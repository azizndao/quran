package org.quran.domain.quran.workers

import android.content.Context
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import kotlinx.coroutines.flow.first
import org.alquran.hafs.model.VerseTranslation
import org.alquran.hafs.model.toDbModel
import org.alquran.hafs.repository.VerseRepository
import org.quran.datastore.repositories.QuranPreferencesRepository
import org.quran.datastore.serializers.DEFAULT_QURAN_FONT_VERSION
import org.quran.datastore.serializers.DEFAULT_TRANSLATION_IDS
import org.quran.domain.quran.R
import org.quran.network.model.ApiVerse
import org.quran.network.model.ApiVerseTranslation
import org.quran.network.services.VerseApiService

class DownloadVersesWorker(
    private val verseRepository: VerseRepository,
    private val apiService: VerseApiService,
    private val quranPreferences: QuranPreferencesRepository,
    context: Context,
    params: WorkerParameters
) : DownloadWorker(context, params) {
    override suspend fun doWork(): Result = try {

        val version = inputData.getInt(PARAM_VERSION, DEFAULT_QURAN_FONT_VERSION)

        val translationIds = quranPreferences.getSelectedTranslations().first().map { it.id }
            .ifEmpty { DEFAULT_TRANSLATION_IDS }

        for (juz in 1..30) {
            val verseList = apiService.downloadVerseByJuz(juz, version, translationIds)

            val translations = verseList.fold(mutableListOf<VerseTranslation>()) { acc, apiVerse ->
                acc.addAll(apiVerse.translations.map(ApiVerseTranslation::toDbModel))
                acc
            }
            verseRepository.insertTranslations(applicationContext, version, translations)
            verseRepository.insertVerses(verseList.map(ApiVerse::toDbModel), version)

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