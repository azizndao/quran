package org.quran.translation.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import org.quran.translation.api.models.LanguagesResponse
import org.quran.translation.api.models.QuranTranslationsResponse
import org.quran.translation.api.models.TranslationsResponse
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


internal interface TranslationApiService {

  @GET("resources/languages")
  suspend fun getAvailableLanguages(@Query("language") language: String = "en"): LanguagesResponse

  @GET("resources/translations")
  suspend fun getAvailableTranslations(@Query("language") language: String = "en"): TranslationsResponse

  @GET("quran/translations/{id}")
  suspend fun getVerses(
    @Path("id") translationId: Int,
    @Query("fields") fields: String = "chapter_id,verse_number"
  ): QuranTranslationsResponse

  companion object {
    fun create(json: Json): TranslationApiService {
      val contentType = MediaType.get("application/json")
      return Retrofit.Builder()
        .baseUrl("https://api.quran.com/api/v4/")
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()
        .create(TranslationApiService::class.java)
    }
  }
}