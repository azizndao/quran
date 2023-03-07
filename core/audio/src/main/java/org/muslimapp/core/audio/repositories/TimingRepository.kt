package org.muslimapp.core.audio.repositories

import android.content.Context
import arg.quran.models.audio.AyaTiming
import arg.quran.models.audio.aya
import arg.quran.models.audio.sura
import org.muslimapp.core.audio.databases.TimingDatabase
import org.muslimapp.core.audio.datasources.QariDataSource
import org.quran.network.audio.AudioApiService
import timber.log.Timber

class TimingRepository(
  private val context: Context,
  private val audioApiService: AudioApiService,
) {

  private var timings: List<AyaTiming> = emptyList()

  suspend fun getPosition(reciter: String, sura: Int, startAyah: Int): Long {
    if (timings.isEmpty() || timings.first().sura != sura) {
      timings = getSuraTimings(reciter, sura)
    }

    if (startAyah == 1) return 0L
    return timings.find { it.aya == startAyah }?.duration ?: 0L
  }

  suspend fun getSuraTimings(slug: String, surah: Int): List<AyaTiming> {
    val timingDao = TimingDatabase.getInstance(context, slug).timingDao

    return timingDao.getTimingBySura(surah).ifEmpty {
      downloadTimingData(slug)
      timingDao.getTimingBySura(surah)
    }
  }

  suspend fun downloadTimingData(slug: String) {
    Timber.d("Downloading timing data for $slug")
    val qari = QariDataSource.find { it.slug == slug }!!
    val timings = audioApiService.getTimings(qari.id)
    val database = TimingDatabase.getInstance(context, slug)
    database.timingDao.insertTimings(timings)
  }

  suspend fun getTiming(reciterId: String, sura: Int, position: Long): AyaTiming? {

    if (timings.isEmpty() || timings.first().sura != sura) {
      timings = getSuraTimings(reciterId, sura)
    }

    val timing = if (position <= timings.first().duration) {
      timings.first()
    } else {
      timings.find { timing -> timing.segments.any { position in it.startDuration..it.endDuration } }
    }

    return timing
  }
}