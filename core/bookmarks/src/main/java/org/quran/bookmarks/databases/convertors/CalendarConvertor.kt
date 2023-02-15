package org.quran.bookmarks.databases.convertors

import androidx.room.TypeConverter
import java.util.*

class CalendarConvertor {

    @TypeConverter
    fun decode(value: Long?) = value?.let { Calendar.getInstance().apply { timeInMillis = it } }

    @TypeConverter
    fun encode(value: Calendar?): Long? = value?.timeInMillis
}