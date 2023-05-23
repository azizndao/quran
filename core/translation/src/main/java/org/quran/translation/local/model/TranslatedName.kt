package org.quran.translation.local.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TranslatedName(
  @SerialName("language_name") val languageName: String,
  val name: String,
)
