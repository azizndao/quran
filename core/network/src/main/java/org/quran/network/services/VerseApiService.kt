package org.quran.network.services

import org.quran.network.model.ApiVerse
import java.io.File

interface VerseApiService {

    suspend fun downloadFont(page: Int, verse: Int, file: File)

    suspend fun downloadVerseByPage(
        page: Int,
        version: Int,
        translations: List<Int>,
        language: String = "en"
    ): List<ApiVerse>

    suspend fun downloadVerseByChapter(
        chapter: Int,
        version: Int,
        translations: List<Int>,
        language: String = "en"
    ): List<ApiVerse>

    suspend fun downloadVerseByJuz(
        juz: Int,
        version: Int,
        translations: List<Int>,
        language: String = "en"
    ): List<ApiVerse>
}