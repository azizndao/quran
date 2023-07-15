package org.quran.core.audio.repositories

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.quran.core.audio.R
import org.quran.core.audio.api.AudioApiService
import org.quran.core.audio.databases.TimingDatabase
import org.quran.core.audio.models.AyahTiming
import timber.log.Timber

class TimingHelper(
  private val context: Context,
  private val apiService: AudioApiService,
) {

  private var timings: MutableMap<String, List<AyahTiming>> = mutableMapOf()

  suspend fun getPosition(reciter: String, sura: Int, startAyah: Int): Long {
    var ayahTimings = timings[reciter]
    if (ayahTimings == null || ayahTimings.first().sura != sura) {
      timings[reciter] = getSurahTimings(reciter, sura) ?: return 0L
      ayahTimings = timings[reciter]
    }

    if (startAyah == 1) return 0L

    return ayahTimings?.find { it.ayah == startAyah }?.time ?: 0L
  }

  suspend fun getSurahTimings(reciterId: String, surah: Int): List<AyahTiming>? {
    val timingDao = TimingDatabase.getInstance(context, reciterId).timingDao
    try {
      return timingDao.getTimingBySura(surah).ifEmpty {
        downloadTimingData(reciterId)
        timingDao.getTimingBySura(surah)
      }
    } catch (e: Exception) {
      Timber.e(e)
      withContext(Dispatchers.Main) {
        Toast.makeText(
          context,
          context.getString(R.string.error_download_recitation_data),
          Toast.LENGTH_SHORT
        ).show()
      }
      return null
    }
  }

  private suspend fun downloadTimingData(reciterId: String) {
    val timings = apiService.getTimingsData(reciterId)
    val database = TimingDatabase.getInstance(context, reciterId)
    database.timingDao.insertTimings(timings)
  }

  suspend fun getTiming(reciterId: String, sura: Int, currentPosition: Long): AyahTiming? {

    var reciterTimings = timings[reciterId]

    if (reciterTimings.isNullOrEmpty() || reciterTimings.first().sura != sura) {
      reciterTimings = getSurahTimings(reciterId, sura) ?: return null
      timings[reciterId] = reciterTimings
    }

    if (reciterTimings.isEmpty()) return null

    val timing = if (currentPosition <= reciterTimings.first().time) {
      reciterTimings.first()
    } else {
      reciterTimings.lastOrNull { timing -> timing.time <= currentPosition }
    }

    return timing
  }

}