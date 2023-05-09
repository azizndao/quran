package org.quran.network.workers

import android.content.Context
import androidx.work.Constraints
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.quran.network.R
import org.quran.network.utils.ZipUtils
import org.quran.network.workers.base.LongRunningDownloadWorker
import kotlin.properties.Delegates

class ExtractArchiveWorker(
  context: Context,
  params: WorkerParameters
) : LongRunningDownloadWorker(context, params) {

  private var notificationId by Delegates.notNull<Int>()

  override suspend fun doWork() = withContext(Dispatchers.IO) {
    val urlStr = inputData.getString(PARAM_PATH) ?: return@withContext Result.failure()
    val savePath = inputData.getString(PARAM_DIRECTION) ?: return@withContext Result.failure()
    notificationId = urlStr.hashCode()

    val message = inputData.getString(PARAM_MESSAGE)
      ?: applicationContext.getString(R.string.extracting)

    notificationBuilder = notificationBuilder
      .setContentTitle(message)
      .setSmallIcon(R.drawable.zip)


    setForeground(ForegroundInfo(notificationId, notificationBuilder.build()))

    ZipUtils.unzipFile(urlStr, savePath, 9) { _, processed, total ->
      notificationBuilder = notificationBuilder.setProgress(total, processed, false)
      setForeground(ForegroundInfo(notificationId, notificationBuilder.build()))
      setProgress(workDataOf(PARAM_PROCESSED to processed, PARAM_TOTAL to total))
    }

    Result.success()
  }

  companion object {

    const val PARAM_PROCESSED = "processed"
    const val PARAM_TOTAL = "total"

    private const val PARAM_MESSAGE = "message"
    private const val PARAM_PATH = "path"
    private const val PARAM_DIRECTION = "save_dir"

    fun create(url: String, savePath: String): OneTimeWorkRequest {
      val constrains = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

      val inputData = workDataOf(
        PARAM_PATH to url,
        PARAM_DIRECTION to savePath
      )

      return OneTimeWorkRequestBuilder<ExtractArchiveWorker>()
        .setConstraints(constrains)
        .setInputData(inputData)
        .build()
    }
  }

}