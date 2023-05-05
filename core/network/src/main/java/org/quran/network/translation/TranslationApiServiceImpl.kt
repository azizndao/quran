package org.quran.network.translation

import arg.quran.models.Language
import arg.quran.models.quran.Verse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.quran.network.translation.models.ApiTranslation
import org.quran.network.translation.models.LanguagesResponse
import org.quran.network.translation.models.QuranTranslationsResponse
import org.quran.network.translation.models.TranslationsResponse
import timber.log.Timber

internal class TranslationApiServiceImpl(
  private val httpClient: HttpClient,
) : TranslationApiService {

  private val baseUrl = "https://api.quran.com/api/v4"

  override suspend fun getAvailableLanguages(language: String): List<Language> {
    return httpClient.get("$baseUrl/resources/languages")
      .body<LanguagesResponse>().languages
  }

  override suspend fun getAvailableTranslations(language: String): List<ApiTranslation> {
    val languages = getAvailableLanguages(language)
    return httpClient.get("$baseUrl/resources/translations")
      .body<TranslationsResponse>().translations
  }

  override suspend fun getVerses(translationId: Int): List<Verse> {
    Timber.d("Downloading translation id = $translationId")
    return httpClient.get("$baseUrl/quran/translations/$translationId") {
      parameter("fields", "page_number,verse_key,verse_number")
    }.body<QuranTranslationsResponse>().translations
      .also { Timber.d("Finish downloading translation id = $translationId") }
  }
}