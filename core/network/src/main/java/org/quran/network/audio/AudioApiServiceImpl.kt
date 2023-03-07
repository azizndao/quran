package org.quran.network.audio

import arg.quran.models.audio.AudioFile
import arg.quran.models.audio.AyaTiming
import arg.quran.models.audio.WordSegment
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.quran.network.audio.models.ApiAyaTiming
import org.quran.network.audio.models.AudioFilesResponse

internal class AudioApiServiceImpl(
  private val httpClient: HttpClient,
) : AudioApiService {

  private val baseUrl = "https://api.quran.com/api/v4"

  override suspend fun getAudioFiles(reciterId: Int, language: String): List<AudioFile> {
    val response = httpClient.get("$baseUrl/chapter_recitations/$reciterId") {
      parameter("language", language)
    }
    return response.body<AudioFilesResponse<AudioFile>>().audioFiles
  }

  override suspend fun getTimings(reciterId: Int, language: String): List<AyaTiming> {
    val response = httpClient.get("$baseUrl/quran/recitations/$reciterId") {
      parameter("language", language)
      parameter("fields", "segments,duration")
    }
    return response.body<AudioFilesResponse<ApiAyaTiming>>()
      .audioFiles
      .groupBy { it.key.sura }
      .flatMap { (sura, ayat) ->

        var duration = 0L

        ayat.map { aya ->
          val segments = aya.segments.map { segment ->
            WordSegment(
              position = segment[1].toInt(),
              sura = sura,
              aya = aya.key.aya,
              startDuration = duration + segment[2],
              endDuration = duration + segment[3]
            )
          }

          duration += aya.duration * 1000

          if (aya.segments.last()[3] > duration) {
            duration = segments.last().endDuration
          }

          AyaTiming(
            url = aya.url,
            verseKey = aya.key,
            duration = duration,
            segments = segments
          )
        }
      }
  }
}