package org.quran.network.audio

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import org.quram.common.source.PageProvider
import org.quram.common.utils.ZipUtils
import timber.log.Timber
import java.io.File

internal class AudioApiServiceImpl(
  private val httpClient: HttpClient,
  private val pageProvider: PageProvider,
) : AudioApiService {


  override suspend fun getTimingsDatabase(slug: String): File {
    val baseUrl = pageProvider.getAudioDatabasesBaseUrl()
    val desDirectory = File(pageProvider.getDatabaseDirectoryName())
    if (!desDirectory.exists()) desDirectory.mkdirs()

    return httpClient.prepareGet("$baseUrl$slug.zip").execute { httpResponse ->
      val channel: ByteReadChannel = httpResponse.body()

      val zipFile = File(desDirectory, "$slug.zip")
      zipFile.deleteOnExit()

      while (!channel.isClosedForRead) {
        val packet = channel.readRemaining(DEFAULT_BUFFER_SIZE.toLong())
        while (!packet.isEmpty) {
          val bytes = packet.readBytes()
          zipFile.appendBytes(bytes)
          Timber.d("Received ${zipFile.length()} bytes from ${httpResponse.contentLength()}")
        }
      }
      Timber.d("A file saved to ${zipFile.path}")
      ZipUtils.unzipFile(zipFile.path, desDirectory.path, 0) { obj, pro, to ->
        Timber.d("$obj: Unzipping database processed = $pro/$to")
      }
      zipFile.delete()
      File(desDirectory, "$slug.db")
    }
  }
}