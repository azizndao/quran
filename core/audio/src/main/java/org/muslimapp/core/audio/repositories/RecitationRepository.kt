package org.muslimapp.core.audio.repositories

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.offline.Download
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import org.muslimapp.core.audio.datasources.RecitationsDataSource
import org.muslimapp.core.audio.datasources.RecitersDataSource
import org.muslimapp.core.audio.models.MediaId
import org.alquran.audio.models.Reciter
import org.alquran.extensions.toUri
import org.quram.common.model.Sura
import org.quram.common.repositories.SurahRepository


class RecitationRepository internal constructor(
    private val recitationsDataSource: RecitationsDataSource,
    private val surahRepository: SurahRepository,
) {

    private fun downloadRecitation(mediaItems: MediaItem) {
        recitationsDataSource.downloadRecitation(mediaItems)
    }

    fun downloadRecitation(reciter: Reciter, sura: Sura) {
        return recitationsDataSource.downloadRecitation(reciter, sura)
    }

    fun downloadRecitation(sura: Int, reciterId: String) {
        recitationsDataSource.downloadRecitation(
            reciter = getReciter(reciterId),
            sura = surahRepository.getSurah(sura)
        )
    }

    fun getAllDownloadedRecitations(): Flow<List<Download>> {
        return recitationsDataSource.getDownloadedRecitations()
    }

    fun getDownloadedRecitations(reciter: String): Flow<List<Download>> {
        return recitationsDataSource.getDownloadedRecitations(reciter)
    }

    fun getRecitations(reciterId: String): List<MediaItem> {
        val mediaItems = mutableListOf<MediaItem>()

        for (sura in 1..114) {
            val suraName = surahRepository.getSurah(sura).nameSimple
            val mediaItem = buildMediaItem(getReciter(reciterId), suraName, sura)
            mediaItems.add(mediaItem)
        }
        return mediaItems
    }

    suspend inline fun getSurahRecitation(surah: Int, reciterId: String): MediaItem {
        return getSurahRecitation(surah = surah, reciter = getReciter(reciterId))
    }

    suspend fun getSurahRecitation(surah: Int, reciter: Reciter): MediaItem {
        val suraName = surahRepository.getSurah(surah).nameSimple
        val mediaItem = buildMediaItem(reciter, suraName, surah)
        coroutineScope { downloadRecitation(mediaItem) }
        return mediaItem
    }

    private fun buildMediaItem(
        reciter: Reciter,
        surahName: String,
        surah: Int,
    ): MediaItem {

        val mediaUri = Uri.parse("${reciter.subfolder}/${surah.toString().padStart(3, '0')}.mp3")

        val metadata = MediaMetadata.Builder()
            .setTitle(surahName)
            .setArtist(reciter.name)
            .setArtworkUri(reciter.image.toUri())
            .setTrackNumber(surah)
            .setSubtitle(reciter.name)
            .setIsPlayable(true)
            .build()
        val mediaId = MediaId(reciter = reciter.id, sura = surah, ayah = 0)

        val requestMetadata = MediaItem.RequestMetadata.Builder()
            .setMediaUri(mediaUri)
            .build()

        return MediaItem.Builder()
            .setMediaId(mediaId.toString())
            .setUri(mediaUri)
            .setMediaMetadata(metadata)
            .setRequestMetadata(requestMetadata)
            .build()
    }

    fun getReciter(id: String) = RecitersDataSource.find { it.id == id }!!

    fun getInProgressDownloads() = recitationsDataSource.getInProgressDownloads()

    fun getAllReciters(): List<Reciter> = RecitersDataSource
}