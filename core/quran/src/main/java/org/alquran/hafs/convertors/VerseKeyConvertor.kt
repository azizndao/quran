package org.alquran.hafs.convertors

import androidx.room.TypeConverter
import org.quram.common.model.VerseKey

class VerseKeyConvertor {

    @TypeConverter
    fun encode(key: VerseKey): String = "$key"

    @TypeConverter
    fun decode(str: String): VerseKey = VerseKey.fromString(str)!!
}