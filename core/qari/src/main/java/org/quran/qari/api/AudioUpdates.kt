package org.quran.qari.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AudioUpdates(
  @SerialName("current_revision") val currentRevision: Int,
  val updates: List<AudioSetUpdate> = emptyList()
)
