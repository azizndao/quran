package org.quran.translation.local

import androidx.room.TypeConverter
import org.quran.translation.local.model.TextDirection

internal class TextDirectionConverter {

  @TypeConverter
  fun encode(value: TextDirection): String = value.value

  @TypeConverter
  fun decode(value: String): TextDirection = TextDirection.parse(value)
}
