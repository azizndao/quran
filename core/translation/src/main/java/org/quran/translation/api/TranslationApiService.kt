package org.quran.translation.api

import org.quran.translation.api.model.LanguagesResponse
import org.quran.translation.api.model.QuranTranslationsResponse
import org.quran.translation.api.model.TranslationsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface TranslationApiService {

  @GET("resources/languages")
  suspend fun getAvailableLanguages(@Query("language") language: String = "en"): LanguagesResponse

  @GET("resources/translations")
  suspend fun getAvailableTranslations(@Query("language") language: String = "en"): TranslationsResponse

  @GET("quran/translations/{id}")
  suspend fun getAyahTranslations(
    @Path("id") translationId: Int,
    @Query("fields") fields: String = "chapter_id,verse_number"
  ): QuranTranslationsResponse
}