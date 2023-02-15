package org.quran.translation.databases.converstors

import androidx.room.TypeConverter
import org.quram.common.model.VerseKey

class VerseKeyConvertor {

    @TypeConverter
    fun encode(tag: VerseKey): String = VerseKey.toString()

    @TypeConverter
    fun decode(str: String): VerseKey = VerseKey.fromString(str)!!
}