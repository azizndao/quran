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
import org.quran.network.workers.base.LongRunningDownloadWorker
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.properties.Delegates

class DownloadFileWorker(
  context: Context,
  params: WorkerParameters
) : LongRunningDownloadWorker(context, params) {

  private var notificationId by Delegates.notNull<Int>()

  override suspend fun doWork() = withContext(Dispatchers.IO) {
    val urlStr = inputData.getString(PARAM_URL) ?: return@withContext Result.failure()
    val savePath = inputData.getString(PARAM_SAVE_PATH) ?: return@withContext Result.failure()
    notificationId = urlStr.hashCode()

    val message = inputData.getString(PARAM_MESSAGE)
      ?: applicationContext.getString(R.string.donwloading)

    val notification = notificationBuilder.setContentTitle(message)
      .build()

    setForeground(ForegroundInfo(notificationId, notification))

    lateinit var connection: HttpURLConnection
    try {
      val url = URL(urlStr)
      connection = url.openConnection() as HttpURLConnection
      connection.connect()

      if (connection.responseCode != HttpURLConnection.HTTP_OK) {
        Result.failure(
          workDataOf(
            PARAM_ERROR_CODE to connection.responseCode,
            PARAM_ERROR_MESSAGE to connection.responseMessage
          )
        )
      }

      // this will be useful to display download percentage
      // might be -1: server did not report the length
      val fileLength = connection.contentLength

      // download the file
      connection.inputStream?.use { input ->
        FileOutputStream(savePath).use { output ->
          val data = ByteArray(4096)
          var total: Long = 0
          var count: Int
          while (input.read(data).also { count = it } != -1) {
            total += count.toLong()
            // publishing the progress....
            if (fileLength > 0) {
              publishProgress(fileLength, total)
            }
            output.write(data, 0, count)
          }
        }
      }
    } catch (e: Exception) {
      Result.failure(
        workDataOf(PARAM_ERROR_MESSAGE to e.toString())
      )
    } finally {
      connection.disconnect()
    }
    Result.success()
  }

  private suspend fun publishProgress(fileLength: Int, total: Long) {
    setProgress(workDataOf("progress" to fileLength, "total" to total))
    val notification = notificationBuilder
      .setProgress(100, (total * 100 / fileLength).toInt(), false)
      .build()
    setForeground(ForegroundInfo(notificationId, notification))
  }


  companion object {
    private const val PARAM_MESSAGE = "message"
    private const val PARAM_URL = "url"
    private const val PARAM_SAVE_PATH = "save_path"

    private const val PARAM_ERROR_MESSAGE = "error_message"
    private const val PARAM_ERROR_CODE = "error_code"

    fun create(url: String, savePath: String, message: String? = null): OneTimeWorkRequest {
      val constrains = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

      val inputData = workDataOf(
        PARAM_MESSAGE to message,
        PARAM_URL to url,
        PARAM_SAVE_PATH to savePath
      )

      return OneTimeWorkRequestBuilder<DownloadFileWorker>()
        .setConstraints(constrains)
        .setInputData(inputData)
        .build()
    }
  }
}