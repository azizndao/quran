package org.quran.translation.databases

import androidx.room.TypeConverter
import arg.quran.models.quran.VerseKey

class VerseKeyConvertor {

  @TypeConverter
  fun encode(tag: VerseKey): String = tag.toString()

  @TypeConverter
  fun decode(str: String): VerseKey = VerseKey.fromString(str)!!
}