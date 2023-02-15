package org.quran.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.quram.common.model.VerseKey

@Serializable
data class ApiVerseTranslation(
    val id: Int,
    @SerialName("resource_id") val authorId: Int,
    @SerialName("resource_name") val authorName: String,
    @SerialName("verse_key") val key: VerseKey,
    val text: String,
)