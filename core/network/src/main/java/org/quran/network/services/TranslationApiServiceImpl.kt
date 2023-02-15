package org.quran.network.services

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.quran.network.model.ApiTranslation
import org.quran.network.model.ApiVerse
import org.quran.network.model.ApiVerseTranslation
import org.quran.network.model.Language
import org.quran.network.model.LanguagesResponse
import org.quran.network.model.PaginatedResponse
import org.quran.network.model.QuranTranslationsResponse
import org.quran.network.model.TranslationsResponse
import timber.log.Timber

internal class TranslationApiServiceImpl(
    private val httpClient: HttpClient,
) : TranslationApiService {

    override suspend fun getAvailableLanguages(language: String): List<Language> {
        return httpClient.get("https://api.quran.com/api/v4/resources/languages")
            .body<LanguagesResponse>().languages
    }

    override suspend fun getAvailableTranslations(language: String): List<ApiTranslation> {
        return httpClient.get("https://api.quran.com/api/v4/resources/translations")
            .body<TranslationsResponse>().translations
    }

    override suspend fun getAyahTranslations(translationId: Int): List<ApiVerseTranslation> {
        Timber.d("Downloading translation id = $translationId")
        return httpClient.get("https://api.quran.com/api/v4/quran/translations/$translationId") {
            parameter("fields", "chapter_id,verse_number")
        }.body<QuranTranslationsResponse>().translations
            .also { Timber.d("Finish downloading translation id = $translationId") }
    }

    override suspend fun downloadVerseByPage(
        page: Int,
        translation: Int,
        language: String,
    ): List<ApiVerse> {
        val response = httpClient.get("https://api.quran.com/api/v4/verses/by_page/$page") {
            parameter("language", language)
            defaultParams()
        }
        return response.body<PaginatedResponse<ApiVerse>>().verses
    }

    override suspend fun downloadVerseByChapter(
        chapter: Int,
        translation: Int,
        language: String
    ): List<ApiVerse> {
        val response = httpClient.get("https://api.quran.com/api/v4/verses/by_chapter/$chapter") {
            parameter("language", language)
            defaultParams()
        }
        return response.body<PaginatedResponse<ApiVerse>>().verses
    }

    override suspend fun downloadVerseByJuz(
        juz: Int,
        translation: Int,
        language: String
    ): List<ApiVerse> {
        val response = httpClient.get("https://api.quran.com/api/v4/verses/by_juz/$juz") {
            parameter("language", language)
            defaultParams()
        }
        return response.body<PaginatedResponse<ApiVerse>>().verses
    }

    private fun HttpRequestBuilder.defaultParams() {
        parameter("page", 1)
        parameter("per_page", 2000)
        parameter("words", false)
        parameter("translation_fields", "resource_name")
    }
}