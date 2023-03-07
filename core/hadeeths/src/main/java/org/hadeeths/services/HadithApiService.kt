package org.hadeeths.services

import org.hadeeths.models.*
import retrofit2.http.GET
import retrofit2.http.Query

internal interface HadithApiService {

  @GET("categories/roots")
  suspend fun getCategories(@Query("language") language: String): List<ApiHadithCategory>

  @GET("hadeeths/list")
  suspend fun paginateHadeethSummaries(
    @Query("category_id") categoryId: Int,
    @Query("language") language: String,
    @Query("page") page: Int,
    @Query("per_page") perPage: Int,
  ): HadithSummaryPage

  @GET("hadeeths/one?language=ar")
  suspend fun getHadeeth(@Query("id") id: Int): Hadith

  @GET("hadeeths/one")
  suspend fun getTranslatedHadeeth(
    @Query("id") id: Int,
    @Query("language") language: String
  ): TranslatedHadith

  @GET("languages")
  suspend fun getAllLanguages(): List<HadithLanguage>
}