package org.muslimapp.core.audio.datasources

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.*
import androidx.media3.exoplayer.scheduler.Requirements
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import org.alquran.audio.MediaDownloadService
import org.muslimapp.core.audio.models.MediaId
import org.alquran.audio.models.Reciter
import org.quram.common.model.Sura
import timber.log.Timber

@androidx.annotation.OptIn(UnstableApi::class)
internal class RecitationsDataSource (
    private val downloadManager: DownloadManager,
     private val context: Context,
) {

    private val tag = this::class.simpleName!!

    fun getDownloadedRecitations() = downloadManager.filter { true }

    fun getDownloadedRecitations(reciter: String) = downloadManager.filter {
        val b = MediaId.fromString(it.request.id).reciter == reciter
        if (b) {
            Timber.tag(tag).d(it.request.id)
        }
        b
    }

    @SuppressLint("UnsafeOptInUsageError")
    fun downloadRecitation(reciter: Reciter, sura: Sura) {

        val mediaId = MediaId(sura = sura.number, reciter = reciter.id, ayah = 0).toString()

        val mediaUri =
            Uri.parse("${reciter.subfolder}/${sura.number.toString().padStart(3, '0')}.mp3")

        val requestMetadata = MediaItem.RequestMetadata.Builder()
            .setMediaUri(mediaUri)
            .build()

        val item = MediaItem.Builder()
            .setMediaId(mediaId)
            .setUri(mediaUri)
            .setRequestMetadata(requestMetadata)
            .build()

        downloadRecitation(item)
    }

    fun downloadRecitation(mediaItem: MediaItem) {
        val downloadRequest = DownloadRequest
            .Builder(mediaItem.mediaId, mediaItem.requestMetadata.mediaUri!!)
            .build()


        setRequirements()

        DownloadService.sendAddDownload(
            context,
            MediaDownloadService::class.java,
            downloadRequest,
            true
        )
    }

    private fun setRequirements() {
        val requirements = Requirements(
            Requirements.DEVICE_STORAGE_NOT_LOW and Requirements.NETWORK
        )

        DownloadService.sendSetRequirements(
            context,
            MediaDownloadService::class.java,
            requirements,
            true,
        )
    }

    @SuppressLint("UnsafeOptInUsageError")
    fun getInProgressDownloads() = downloadManager.filter(
        Download.STATE_DOWNLOADING,
        Download.STATE_QUEUED
    )
}


@SuppressLint("UnsafeOptInUsageError")
@OptIn(FlowPreview::class)
fun DownloadManager.filter(
    @Download.State vararg state: Int,
    predicate: (Download) -> Boolean = { true },
): Flow<List<Download>> = callbackFlow {

    send(downloadIndex.parse(*state, predicate = predicate))

    var job: Job? = null

    val listener = object : DownloadManager.Listener {
        override fun onDownloadChanged(
            downloadManager: DownloadManager,
            download: Download,
            finalException: Exception?,
        ) {
            job?.cancel()
            job = launch {
                if (finalException != null) throw finalException

                send(downloadIndex.parse(*state, predicate = predicate))
            }
        }
    }

    addListener(listener)

    awaitClose { removeListener(listener) }
}.debounce(200L)

@SuppressLint("UnsafeOptInUsageError")
private suspend fun DownloadIndex.parse(
    @Download.State vararg states: Int,
    predicate: (Download) -> Boolean = { true },
) = withContext(Dispatchers.IO) {
    getDownloads(*states).use { cursor ->
        buildList {
            while (cursor.moveToNext()) {
                val download = cursor.download
                if (predicate(download)) {
                    add(download)
                }
            }
        }
    }
}