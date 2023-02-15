package org.quran.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ApiTranslation(
    val id: Int,
    val name: String,

    @SerialName("author_name")
    val authorName: String,

    @SerialName("language_name")
    val languageName: String,

    val slug: String?,
)

val ApiTranslation.fallbackSlug get() = "quran.$languageName.$id.db"