package org.quran.network.translation

import arg.quran.models.Language
import arg.quran.models.quran.VerseTranslation
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.quran.network.translation.models.*
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
      .mapNotNull {
        when (val lang =
          languages.find { l -> l.name.lowercase() == it.language.lowercase() }) {
          null -> null
          else -> it.copy(language = lang.isoCode, direction = lang.direction)
        }
      }
  }

  override suspend fun getAyahTranslations(translationId: Int): List<VerseTranslation> {
    Timber.d("Downloading translation id = $translationId")
    return httpClient.get("$baseUrl/quran/translations/$translationId") {
      parameter("fields", "page_number,verse_key,verse_number")
    }.body<QuranTranslationsResponse>().translations
      .also { Timber.d("Finish downloading translation id = $translationId") }
  }
}