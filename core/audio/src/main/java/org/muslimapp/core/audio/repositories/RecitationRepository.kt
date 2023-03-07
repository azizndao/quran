package org.muslimapp.core.audio.repositories

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.offline.Download
import arg.quran.models.Sura
import arg.quran.models.audio.Qari
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import org.muslimapp.core.audio.datasources.QariDataSource
import org.muslimapp.core.audio.datasources.RecitationsDataSource
import org.muslimapp.core.audio.models.MediaId
import org.quram.common.extensions.toUri
import org.quram.common.repositories.SurahRepository


class RecitationRepository internal constructor(
  private val recitationsDataSource: RecitationsDataSource,
  private val surahRepository: SurahRepository,
  private val context: Context,
) {

  fun getReciter(slug: String): Qari = QariDataSource.find { it.slug == slug }!!

  fun getAllReciters(): List<Qari> = QariDataSource

  private fun downloadRecitation(mediaItems: MediaItem) {
    recitationsDataSource.downloadRecitation(mediaItems)
  }

  fun downloadRecitation(qari: Qari, sura: Sura) {
    return recitationsDataSource.downloadRecitation(qari, sura)
  }

  fun downloadRecitation(sura: Int, slug: String) {
    recitationsDataSource.downloadRecitation(
      qari = getReciter(slug),
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
      val reciter = getReciter(reciterId)
      val mediaItem = buildMediaItem(reciter, suraName, sura)
      mediaItems.add(mediaItem)
    }
    return mediaItems
  }

  suspend inline fun getSurahRecitation(surah: Int, reciterId: String): MediaItem {
    return getSurahRecitation(surah = surah, qari = getReciter(reciterId))
  }

  suspend fun getSurahRecitation(surah: Int, qari: Qari): MediaItem {
    val suraName = surahRepository.getSurah(surah).nameSimple
    val mediaItem = buildMediaItem(qari, suraName, surah)
    coroutineScope { downloadRecitation(mediaItem) }
    return mediaItem
  }

  private fun buildMediaItem(
    qari: Qari,
    surahName: String,
    surah: Int,
  ): MediaItem {

    val mediaUri = Uri.parse("${qari.subfolder}/${surah}.mp3")

    val name = context.getString(qari.nameId)
    val metadata = MediaMetadata.Builder()
      .setTitle(surahName)
      .setArtist(name)
      .setArtworkUri(qari.image.toUri())
      .setTrackNumber(surah)
      .setSubtitle(name)
      .setIsPlayable(true)
      .build()
    val mediaId = MediaId(reciter = qari.slug, sura = surah, ayah = 0)

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

  fun getInProgressDownloads() = recitationsDataSource.getInProgressDownloads()
}