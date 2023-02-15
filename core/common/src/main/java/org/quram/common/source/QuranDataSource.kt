package org.quran.hafs.common.source

import org.quram.common.model.VerseKey


interface QuranDataSource {
    val numberOfPages: Int
    val pageForSuraArray: IntArray
    val suraForPageArray: IntArray
    val ayahForPageArray: IntArray
    val pageForJuzArray: IntArray
    val juzDisplayPageArrayOverride: Map<Int, Int>
    val numberOfAyahsForSuraArray: IntArray
    val isMakkiBySuraArray: BooleanArray
    val quarterStartByPage: IntArray
    val quartersArray: Array<VerseKey>
}
