package org.quran.qari.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AudioSetUpdate(
  val path: String,
  @SerialName("database_version") val databaseVersion: Int? = null,
  val files: List<AudioFileUpdate> = emptyList())
