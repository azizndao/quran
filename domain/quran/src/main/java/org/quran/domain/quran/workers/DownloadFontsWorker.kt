package org.quran.domain.quran.workers

import android.content.Context
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import org.quram.common.utils.UriProvider
import org.quran.datastore.serializers.DEFAULT_QURAN_FONT_VERSION
import org.quran.domain.quran.R
import org.quran.network.services.VerseApiService

class DownloadFontsWorker(
    private val apiService: VerseApiService,
    context: Context,
    params: WorkerParameters
) : DownloadWorker(context, params) {


    override suspend fun doWork(): Result {

        val fontVersion = inputData.getInt(VERSION, DEFAULT_QURAN_FONT_VERSION)

        return try {
            for (page in 1..604) {
                val file = UriProvider.getFontFile(applicationContext, page, fontVersion)

                if (file.exists()) continue

                apiService.downloadFont(page, fontVersion, file)
                setForeground(
                    ForegroundInfo(
                        NOTIFICATION_ID,
                        notificationBuilder
                            .setContentTitle(applicationContext.getString(R.string.downloading_quran_fonts))
                            .setProgress(604, page, false).build()
                    )
                )
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            NOTIFICATION_ID, notificationBuilder
                .setContentTitle(applicationContext.getString(R.string.downloading_quran_fonts))
                .build()
        )
    }

    companion object {
        const val NOTIFICATION_ID = 12123
        const val VERSION = "version"
    }
}