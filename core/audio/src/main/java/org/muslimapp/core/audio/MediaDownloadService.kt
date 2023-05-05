package org.muslimapp.core.audio

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.media3.common.util.NotificationUtil
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadNotificationHelper
import androidx.media3.exoplayer.offline.DownloadService
import androidx.media3.exoplayer.scheduler.Requirements
import androidx.media3.exoplayer.scheduler.Scheduler
import androidx.media3.exoplayer.workmanager.WorkManagerScheduler
import org.koin.android.ext.android.inject
import org.muslimapp.core.audio.models.MediaId
import org.quram.common.utils.MuslimsConstants.DOWNLOAD_CHANNEL_ID
import org.quran.audio.R

@SuppressLint("UnsafeOptInUsageError")
class MediaDownloadService : DownloadService(
  FOREGROUND_NOTIFICATION_ID,
  DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
  DOWNLOAD_CHANNEL_ID,
  R.string.download_notification_channel,
  R.string.download_notification_channel_desc
) {

  private val manager by inject<DownloadManager>()

  private val notificationHelper by lazy { DownloadNotificationHelper(this, DOWNLOAD_CHANNEL_ID) }

  override fun getDownloadManager(): DownloadManager {

//        manager.addListener(
//            TerminalStateNotificationHelper(
//                this,
//                notificationHelper,
//                FOREGROUND_NOTIFICATION_ID + 1
//            )
//        )
    return manager
  }

  override fun getScheduler(): Scheduler = WorkManagerScheduler(this, "download scheduler")

  override fun getForegroundNotification(
    downloads: List<Download>,
    notMetRequirements: @Requirements.RequirementFlags Int,
  ): Notification {

    val numberOfSurahs = downloads.map {
      val id = MediaId.fromString(it.request.id)
      "${id.reciter}:${id.sura}"
    }.size

    return notificationHelper.buildProgressNotification(
      this,
      R.drawable.logo,
      null,
      getString(R.string.number_of_surahs, numberOfSurahs),
      downloads,
      notMetRequirements
    )
  }

  /**
   * Creates and displays notifications for downloads when they complete or fail.
   *
   *
   * This helper will outlive the lifespan of a single instance of [MediaDownloadService].
   * It is static to avoid leaking the first [MediaDownloadService] instance.
   */
  private class TerminalStateNotificationHelper(
    context: Context,
    private val notificationHelper: DownloadNotificationHelper,
    firstNotificationId: Int,
  ) : DownloadManager.Listener {

    private val context: Context = context.applicationContext
    private var nextNotificationId: Int = firstNotificationId + 1
    private var pauseNotificationId: Int = firstNotificationId + 2
    private var waitingNotificationId: Int = firstNotificationId + 3
    private var completeNotificationId: Int = firstNotificationId + 4

    override fun onDownloadChanged(
      downloadManager: DownloadManager,
      download: Download,
      finalException: Exception?,
    ) {
      if (download.state == Download.STATE_FAILED) {
        val notification = notificationHelper.buildDownloadFailedNotification(
          context,
          R.drawable.ic_error,
          null,
          Util.fromUtf8Bytes(download.request.data)
        )
        NotificationUtil.setNotification(context, ++completeNotificationId, notification)
      }
    }

    /* override fun onDownloadsPausedChanged(
         downloadManager: DownloadManager,
         downloadsPaused: Boolean
     ) {
         val notification = if (downloadsPaused) {
             notificationHelper.buildDownloadCompletedNotification(
                 context,
                 R.drawable.ic_pause,
                 null,
                 context.getString(R.string.downloaded_idle)
             )
         } else {
             null
         }
         NotificationUtil.setNotification(context, completeNotificationId, notification)
     }*/

    override fun onIdle(downloadManager: DownloadManager) {
      val notification = if (downloadManager.isWaitingForRequirements) {
        null
      } else {
        notificationHelper.buildDownloadCompletedNotification(
          context,
          R.drawable.ic_check_circle,
          null,
          null
        )
      }
      NotificationUtil.setNotification(context, completeNotificationId, notification)
    }

    override fun onWaitingForRequirementsChanged(
      downloadManager: DownloadManager,
      waitingForRequirements: Boolean,
    ) {
      val notification = if (waitingForRequirements) {
        NotificationCompat.Builder(context, DOWNLOAD_CHANNEL_ID)
          .setProgress(0, 0, false)
          .setSmallIcon(R.drawable.ic_download)
          .setContentTitle(context.getString(R.string.waiting_downloads))
          .setAutoCancel(true)
          .setOngoing(false)
          .addAction(R.drawable.ic_clear, context.getString(R.string.cancel), null)
          .build()
      } else {
        null
      }
      NotificationUtil.setNotification(context, waitingNotificationId, notification)
    }
  }

  companion object {
    private const val FOREGROUND_NOTIFICATION_ID = 10257
  }
}