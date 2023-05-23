package org.quran.core.audio.repositories

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.quran.core.audio.api.AudioApiService
import org.muslimapp.core.audio.databases.TimingDatabase
import org.muslimapp.core.audio.models.AyahTiming
import org.quran.core.audio.R
import timber.log.Timber

class TimingRepository(
  private val context: Context,
  private val apiService: AudioApiService,
  private val json: Json,
) {
  private var timings: List<AyahTiming> = emptyList()

  suspend fun getPosition(reciter: String, sura: Int, startAyah: Int): Long {
    if (timings.isEmpty() || timings.first().sura != sura) {
      timings = getSurahTimings(reciter, sura) ?: return 0L
    }

    if (startAyah == 1) return 0L
    return timings.find { it.ayah == startAyah }?.time ?: 0L
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
//    val timings: List<AyahTiming> = json.decodeFromString(content)
    val database = TimingDatabase.getInstance(context, reciterId)
    database.timingDao.insertTimings(timings)
  }

  suspend fun getTiming(reciterId: String, sura: Int, currentPosition: Long): AyahTiming? {

    if (timings.isEmpty() || timings.first().sura != sura) {
      timings = getSurahTimings(reciterId, sura) ?: return null
    }

    if (timings.isEmpty()) return null

    val timing = if (currentPosition <= timings.first().time) {
      timings.first()
    } else {
      timings.lastOrNull { timing -> timing.time <= currentPosition }
    }

    return timing
  }

}