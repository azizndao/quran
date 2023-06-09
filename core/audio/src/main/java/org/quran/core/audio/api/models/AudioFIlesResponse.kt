package org.quran.core.audio.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AudioFilesResponse<FILE>(
  @SerialName("audio_files") val audioFiles: List<FILE>
)
