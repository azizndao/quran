package org.muslimapp.core.audio.repositories

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.muslimapp.core.audio.databases.TimingDatabase
import org.alquran.audio.models.AyahTiming
import org.quram.common.utils.UriProvider

class TimingRepository(
    private val context: Context,
    private val httpClient: HttpClient,
) {
    private var timings: List<AyahTiming> = emptyList()


    suspend fun getPosition(reciter: String, sura: Int, startAyah: Int): Long {
        if (timings.isEmpty() || timings.first().sura != sura) {
            timings = getSurahTimings(reciter, sura)
        }

        if (startAyah == 1) return 0L
        return timings.find { it.ayah == startAyah }?.time ?: 0L
    }

    suspend fun getSurahTimings(reciterId: String, surah: Int): List<AyahTiming> {
        val timingDao = TimingDatabase.getInstance(context, reciterId).timingDao

        return timingDao
            .getTimingBySura(surah)
            .ifEmpty {
                downloadTimingData(reciterId)
                timingDao.getTimingBySura(surah)
            }
    }

    suspend fun downloadTimingData(reciterId: String) {
        val response = httpClient.get(UriProvider.getTimingData(reciterId))
        val timings = response.body<List<AyahTiming>>()
        val database = TimingDatabase.getInstance(context, reciterId)
        database.timingDao.insertTimings(timings)
    }

    suspend fun getTiming(reciterId: String, sura: Int, currentPosition: Long): AyahTiming? {

        if (timings.isEmpty() || timings.first().sura != sura) {
            timings = getSurahTimings(reciterId, sura)
        }

        val timing = if (currentPosition <= timings.first().time) {
            timings.first()
        } else {
            timings.lastOrNull { timing -> timing.time <= currentPosition }
        }

        return timing
    }

}