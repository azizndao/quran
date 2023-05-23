package org.quran.translation.api.model

import kotlinx.serialization.Serializable

@Serializable
internal data class TranslationsResponse(
  val translations: List<ApiTranslation>
)
