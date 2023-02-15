package org.quran.network.services

import org.quran.network.model.ApiTranslation
import org.quran.network.model.ApiVerse
import org.quran.network.model.ApiVerseTranslation
import org.quran.network.model.Language

interface TranslationApiService {

    suspend fun getAvailableLanguages(language: String = "en"): List<Language>

    suspend fun getAvailableTranslations(language: String = "en"): List<ApiTranslation>

    suspend fun getAyahTranslations(translationId: Int): List<ApiVerseTranslation>

    suspend fun downloadVerseByPage(
        page: Int,
        translation: Int,
        language: String = "en"
    ): List<ApiVerse>

    suspend fun downloadVerseByChapter(
        chapter: Int,
        translation: Int,
        language: String = "en"
    ): List<ApiVerse>

    suspend fun downloadVerseByJuz(
        juz: Int,
        translation: Int,
        language: String = "en"
    ): List<ApiVerse>
}