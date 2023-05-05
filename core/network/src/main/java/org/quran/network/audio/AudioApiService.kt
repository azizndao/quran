package org.quran.network.audio

import java.io.File

interface AudioApiService {
  suspend fun getTimingsDatabase(slug: String): File
}