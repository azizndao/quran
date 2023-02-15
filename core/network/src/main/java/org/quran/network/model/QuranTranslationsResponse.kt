package org.quran.network.model

import kotlinx.serialization.Serializable

@Serializable
internal data class QuranTranslationsResponse(
    val translations: List<ApiVerseTranslation>
)
