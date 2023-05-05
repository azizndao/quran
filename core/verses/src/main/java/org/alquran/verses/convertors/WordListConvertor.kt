package org.alquran.verses.convertors

import androidx.room.TypeConverter
import arg.quran.models.quran.QuranWord
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class WordListConvertor {
  @TypeConverter
  fun decode(value: String): List<QuranWord> = Json.decodeFromString(value)

  @TypeConverter
  fun encode(value: List<QuranWord>): String = Json.encodeToString(value)
}