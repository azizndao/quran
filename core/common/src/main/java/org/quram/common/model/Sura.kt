package org.quram.common.model

data class Sura(
    val number: Int,
    val isMakki: Boolean,
    val revelationOrder: Int,
    val nameSimple: String,
    val nameComplex: String,
    val nameArabic: String,
    val ayahCount: Int,
    val pages: IntRange,
)

data class SuraWithTranslation(
    val number: Int,
    val isMakki: Boolean,
    val revelationOrder: Int,
    val nameSimple: String,
    val nameComplex: String,
    val nameArabic: String,
    val ayahCount: Int,
    val pages: IntRange,
    val translation: String,
)

inline val SuraWithTranslation.firstPage get() = pages.first

val Sura.bismillahPre: Boolean get() = number != 1 && number != 9

data class SurahMapping(val juz: Int, val page: Int, val surahs: List<SuraWithTranslation>)