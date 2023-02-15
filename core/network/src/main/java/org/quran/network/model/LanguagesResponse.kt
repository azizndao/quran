package org.quran.network.model

import kotlinx.serialization.Serializable

@Serializable
internal data class LanguagesResponse(
    val languages: List<Language>
)