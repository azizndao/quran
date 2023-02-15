package org.quran.bookmarks.databases.convertors

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.quran.bookmarks.model.BookmarkTag

class BookmarkTagConvertor {

    @TypeConverter
    fun encode(tag: BookmarkTag): String = Json.encodeToString(tag)

    @TypeConverter
    fun decode(str: String): BookmarkTag = Json.decodeFromString(str)
}