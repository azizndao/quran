package org.quran.translation.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ApiTranslation(
  val id: Int,
  val name: String,

  @SerialName("author_name")
  val authorName: String,

  @SerialName("language_name")
  val languageName: String,

  val slug: String?,
)

internal val ApiTranslation.fallbackSlug get() = "quran.$languageName.$id.db"