package org.hadeeths

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hadeeths.models.WordsMeaning
import java.util.*

class ListStringConvertors {

  @TypeConverter
  fun encode(value: List<String>): String = Json.encodeToString(value)

  @TypeConverter
  fun decode(value: String): List<String> = Json.decodeFromString(value)
}

class WordsMeaningConvertors {

  @TypeConverter
  fun encode(value: List<WordsMeaning>): String = Json.encodeToString(value)

  @TypeConverter
  fun decode(value: String): List<WordsMeaning> = Json.decodeFromString(value)
}

class CalendarConvertor {

  @TypeConverter
  fun encode(value: Calendar): Long = value.timeInMillis

  @TypeConverter
  fun decode(value: Long): Calendar = Calendar.getInstance().apply { timeInMillis = value }
}