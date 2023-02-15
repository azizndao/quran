package org.quran.network.services

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import org.quran.network.model.ApiVerse
import org.quran.network.model.PaginatedResponse
import timber.log.Timber
import java.io.File

internal class VerseApiServiceImpl(
    private val httpClient: HttpClient
) : VerseApiService {
    override suspend fun downloadVerseByPage(
        page: Int,
        version: Int,
        translations: List<Int>,
        language: String,
    ): List<ApiVerse> {
        val response = httpClient.get("https://api.quran.com/api/v4/verses/by_page/$page") {
            parameter("language", language)
            defaultParams(version, translations)
        }
        return response.body<PaginatedResponse<ApiVerse>>().verses
    }

    override suspend fun downloadVerseByChapter(
        chapter: Int,
        version: Int,
        translations: List<Int>,
        language: String
    ): List<ApiVerse> {
        val response = httpClient.get("https://api.quran.com/api/v4/verses/by_chapter/$chapter") {
            parameter("language", language)
            defaultParams(version, translations)
        }
        return response.body<PaginatedResponse<ApiVerse>>().verses
    }

    override suspend fun downloadVerseByJuz(
        juz: Int,
        version: Int,
        translations: List<Int>,
        language: String
    ): List<ApiVerse> {
        val response = httpClient.get("https://api.quran.com/api/v4/verses/by_juz/$juz") {
            parameter("language", language)
            defaultParams(version, translations)
        }
        return response.body<PaginatedResponse<ApiVerse>>().verses
    }

    override suspend fun downloadFont(page: Int, version: Int, file: File) {
        try {
            httpClient.prepareGet("https://quran.com/fonts/quran/hafs/v$version/ttf/p$page.ttf")
                .execute { httpResponse ->
                    val channel: ByteReadChannel = httpResponse.body()
                    while (!channel.isClosedForRead) {
                        val packet = channel.readRemaining(DEFAULT_BUFFER_SIZE.toLong())
                        while (!packet.isEmpty) {
                            val bytes = packet.readBytes()
                            file.appendBytes(bytes)
                        }
                    }
                }
            Timber.tag("VerseApiServiceImpl").d("A file saved to ${file.path}")
        } catch (e: Exception) {
            Timber.tag("VerseApiServiceImpl").e(e)
            file.deleteOnExit()
            throw e
        }
    }

    private fun HttpRequestBuilder.defaultParams(version: Int, translations: List<Int>) {
        parameter("page", 1)
        parameter("per_page", 3000)
        parameter("words", true)
        parameter("word_fields", "code_v$version,verse_key")
        parameter("translation_fields", "verse_key,resource_name")
        parameter("translations", translations.joinToString())
        parameter("fields", "v${version}_page")
    }
}