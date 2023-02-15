package org.quran.domain.quran.workers

import android.content.Context
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_NONE
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.quram.common.utils.MuslimsConstants.DOWNLOAD_CHANNEL_ID
import org.quran.domain.quran.R

abstract class DownloadWorker(
    context: Context,
    parameters: WorkerParameters,
) : CoroutineWorker(context, parameters) {

    companion object {
        private const val DOWNLOAD_NOTIFICATION_GROUP = "muslims.DOWNLOAD_NOTIFICATION_GROUP"
    }

    init {
        createNotificationChannel()
    }

    var notificationBuilder = NotificationCompat.Builder(applicationContext, DOWNLOAD_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_download)
        .setProgress(0, 0, true)
        .setCategory(NotificationCompat.CATEGORY_PROGRESS)
        .setOngoing(true)
        .setGroup(DOWNLOAD_NOTIFICATION_GROUP)
        .setShowWhen(false)
        .setSilent(true)


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val description =
                applicationContext.getString(R.string.download_notification_channel_desc)
            val name = applicationContext.getString(R.string.download_notification_channel)
            val channel = NotificationChannelCompat.Builder(DOWNLOAD_CHANNEL_ID, IMPORTANCE_NONE)
                .setName(name)
                .setDescription(description)
                .build()
            NotificationManagerCompat.from(applicationContext)
                .createNotificationChannel(channel)
        }
    }
}