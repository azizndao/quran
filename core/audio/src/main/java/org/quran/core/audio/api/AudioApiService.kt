package org.quran.core.audio.api

import org.quran.core.audio.models.AyahTiming
import retrofit2.http.GET
import retrofit2.http.Path

interface AudioApiService {
  @GET("timings/{slug}.json")
  suspend fun getTimingsData(@Path("slug") slug: String): List<AyahTiming>
}