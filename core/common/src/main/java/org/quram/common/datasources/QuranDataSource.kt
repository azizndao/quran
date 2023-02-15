package org.quram.common.datasources

import org.quram.common.model.Juz
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
    val juzsArray: Array<Juz>

    companion object {
        fun getInstance(version: Int): QuranDataSource {
            return if (version == 1) {
                MadaniV1DataSource.getInstance()
            } else {
                MadaniV2DataSource.getInstance()
            }
        }
    }
}

