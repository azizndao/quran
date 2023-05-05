package org.quran.qari.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AudioFileUpdate(val filename: String, @SerialName("md5") val md5sum: String)
