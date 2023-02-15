package org.alquran.hafs.convertors

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.alquran.hafs.model.Word

class WordListConvertor {
    @TypeConverter
    fun decode(value: String): List<Word> = Json.decodeFromString(value)

    @TypeConverter
    fun encode(value: List<Word>): String = Json.encodeToString(value)
}