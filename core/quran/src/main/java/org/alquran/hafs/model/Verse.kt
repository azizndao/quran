package org.alquran.hafs.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.quram.common.model.VerseKey
import org.quran.network.model.ApiVerse
import org.quran.network.model.ApiWord

@Entity(tableName = "verses", indices = [Index("page")])
data class Verse(
    @PrimaryKey val id: Int,
    val key: VerseKey,
    val hizb: Int,

    val rubElHizb: Int,
    val ruku: Int,
    val manzil: Int,
    val sajdah: Int? = null,
    val page: Int,
    val juz: Int,

    val words: List<Word>,
) {
}

fun ApiVerse.toDbModel(): Verse = Verse(
    id = id,
    key = key,
    hizb = hizb,
    rubElHizb = rubElHizb,
    ruku = ruku,
    manzil = manzil,
    sajdah = sajdah,
    page = v1Page ?: v2Page!!,
    juz = juz,
    words = words.map(ApiWord::toDbModel)
)
