package org.quran.qari.model

data class PartiallyDownloadedSura(val sura: Int, val expectedAyahCount: Int, val downloadedAyat: List<Int>)
