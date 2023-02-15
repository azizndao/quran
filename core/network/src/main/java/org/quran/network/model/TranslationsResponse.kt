package org.quran.network.model

import kotlinx.serialization.*

@Serializable
internal data class TranslationsResponse(
    val translations: List<ApiTranslation>
)
