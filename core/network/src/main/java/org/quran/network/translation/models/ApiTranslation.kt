package org.quran.network.translation.models

import arg.quran.models.Direction
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ApiTranslation(
  val id: Int,
  val name: String,

  @SerialName("author_name")
  val authorName: String,

  @SerialName("language_name")
  val language: String,

  val slug: String?,

  val direction: Direction? = null
)

val ApiTranslation.fallbackSlug get() = "quran.$language.$id.db"
