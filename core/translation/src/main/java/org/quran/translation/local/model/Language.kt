package org.quran.translation.local.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Language(
  val id: Long,
  val name: String,

  @SerialName("iso_code")
  val isoCode: String,

  @SerialName("native_name")
  val nativeName: String,

  val direction: String,

  @SerialName("translations_count")
  val translationsCount: Long,

  @SerialName("translated_name")
  val translatedName: TranslatedName
)
