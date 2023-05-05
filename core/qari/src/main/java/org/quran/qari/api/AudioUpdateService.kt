package org.quran.qari.api

import retrofit2.http.GET
import retrofit2.http.Query

interface AudioUpdateService {
  @GET("/data/audio_updates.php")
  suspend fun getUpdates(@Query("revision") revision: Int): AudioUpdates
}
