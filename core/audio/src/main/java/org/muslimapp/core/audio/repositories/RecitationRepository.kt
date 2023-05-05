package org.muslimapp.core.audio.repositories

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.offline.Download
import arg.quran.models.Sura
import arg.quran.models.audio.Qari
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import org.muslimapp.core.audio.datasources.RecitationsDataSource
import org.muslimapp.core.audio.utils.buildMediaItem
import org.quram.common.repositories.SurahRepository
import org.quram.common.utils.QuranDisplayData


class RecitationRepository internal constructor(
  private val recitationsDataSource: RecitationsDataSource,
  private val surahRepository: SurahRepository,
  private val context: Context,
  val qariRepository: QariRepository,
  private val quranDisplayData: QuranDisplayData,
) {

  private fun downloadRecitation(mediaItems: MediaItem) {
    recitationsDataSource.downloadRecitation(mediaItems)
  }

  fun downloadRecitation(qari: Qari, sura: Sura) {
    return recitationsDataSource.downloadRecitation(qari, sura)
  }

  fun downloadRecitation(sura: Int, slug: String) {
    recitationsDataSource.downloadRecitation(
      qari = qariRepository.getQari(slug),
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
      val reciter = qariRepository.getQari(reciterId)
      val mediaItem = buildMediaItem(
        context,
        reciter,
        quranDisplayData.getSuraAyahString(sura, 1),
        sura
      )
      mediaItems.add(mediaItem)
    }
    return mediaItems
  }

  suspend inline fun getSurahRecitation(surah: Int, reciterId: String): MediaItem {
    return getSurahRecitation(surah = surah, qari = qariRepository.getQari(reciterId))
  }

  suspend fun getSurahRecitation(surah: Int, qari: Qari): MediaItem {
    val suraName = quranDisplayData.getSuraAyahString(surah, 1)
    val mediaItem = buildMediaItem(context, qari, suraName, surah)
    coroutineScope { downloadRecitation(mediaItem) }
    return mediaItem
  }


  fun getInProgressDownloads() = recitationsDataSource.getInProgressDownloads()
}