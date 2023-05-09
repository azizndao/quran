package org.quran.translation.api.models

import kotlinx.serialization.Serializable

@Serializable
internal data class TranslationsResponse(
  val translations: List<ApiTranslation>
)
