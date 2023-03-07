package org.muslimapp.core.audio.databases.convertors

import androidx.room.TypeConverter
import arg.quran.models.audio.WordSegment
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SegmentsListConvertor {

  @TypeConverter
  fun encode(segments: List<WordSegment>): String = Json.encodeToString(segments)

  @TypeConverter
  fun decode(value: String): List<WordSegment> = Json.decodeFromString(value)
}