package org.quran.network.audio.models

import arg.quran.models.audio.AyaTiming
import arg.quran.models.audio.WordSegment
import arg.quran.models.quran.VerseKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiAyaTiming(
  val id: Long = -1,

  @SerialName("verse_key")
  val key: VerseKey,

  val url: String,

  val segments: List<LongArray>,

  val duration: Long
)

fun ApiAyaTiming.toDbModel(): AyaTiming {
  val wordSegments = segments.map {
    WordSegment(
      sura = key.sura,
      aya = key.aya,
      position = it[1].toInt(),
      startDuration = it[2] ,
      endDuration = it[3],
    )
  }
  return AyaTiming(
    verseKey = key,
    url = url,
    duration = duration,
    segments = wordSegments
  )
}