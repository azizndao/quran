package org.quran.core.audio.repositories

import android.net.Uri
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.offline.Download
import arg.quran.models.Sura
import arg.quran.models.audio.Qari
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.quran.core.audio.models.MediaId
import org.quram.common.repositories.SurahRepository
import org.quram.common.utils.QuranDisplayData
import org.quran.core.audio.datasources.RecitationsDataSource
import org.quran.core.audio.models.QariItem


class RecitationRepository internal constructor(
  private val recitationsDataSource: RecitationsDataSource,
  private val surahRepository: SurahRepository,
  private val qariRepository: QariRepository,
  private val quranDisplayData: QuranDisplayData,
) {

  private fun downloadRecitation(mediaItems: MediaItem) {
    recitationsDataSource.downloadRecitation(mediaItems)
  }

  fun downloadRecitation(reciter: Qari, surah: Sura) {
    return recitationsDataSource.downloadRecitation(reciter, surah)
  }

  fun downloadRecitation(sura: Int, slug: String) {
    recitationsDataSource.downloadRecitation(
      reciter = qariRepository.getQari(slug),
      sura = surahRepository.getSurah(sura)
    )
  }

  fun getAllDownloadedRecitations(): Flow<List<Download>> {
    return recitationsDataSource.getDownloadedRecitations()
  }

  fun getDownloadedRecitations(reciter: String): Flow<List<Download>> {
    return recitationsDataSource.getDownloadedRecitations(reciter)
  }

  suspend fun getRecitations(slug: String): List<MediaItem> = withContext(Dispatchers.IO) {
    val mediaItems = mutableListOf<MediaItem>()
    for (sura in 1..114) {
      val suraName = surahRepository.getSurah(sura).nameSimple
      val mediaItem = buildMediaItem(qariRepository.getQariItem(slug), suraName, sura)
      mediaItems.add(mediaItem)
    }
    mediaItems
  }

  suspend fun getSurahRecitation(surah: Int, slug: String): MediaItem {
    return getSurahRecitation(surah = surah, reciter = qariRepository.getQariItem(slug))
  }

  suspend fun getSurahRecitation(surah: Int, reciter: QariItem) = withContext(Dispatchers.IO) {
    val suraName = quranDisplayData.getSuraName(surah, true)
    val mediaItem = buildMediaItem(reciter, suraName, surah)
    coroutineScope { downloadRecitation(mediaItem) }
    mediaItem
  }

  private fun buildMediaItem(
    reciter: QariItem,
    surahName: String,
    surah: Int,
  ): MediaItem {

    val mediaUri = Uri.parse("${reciter.url}/${surah.toString().padStart(3, '0')}.mp3")

    val metadata = MediaMetadata.Builder()
      .setTitle(surahName)
      .setArtist(reciter.name)
      .setArtworkUri(reciter.imageUri?.toUri())
      .setTrackNumber(surah)
      .setSubtitle(reciter.name)
      .setIsPlayable(true)
      .build()
    val mediaId = MediaId(reciter = reciter.path, sura = surah, ayah = 0)

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