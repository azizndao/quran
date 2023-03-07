package org.alquran.hafs.convertors

import androidx.room.TypeConverter
import arg.quran.models.quran.VerseWord
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class WordListConvertor {
  @TypeConverter
  fun decode(value: String): List<VerseWord> = Json.decodeFromString(value)

  @TypeConverter
  fun encode(value: List<VerseWord>): String = Json.encodeToString(value)
}