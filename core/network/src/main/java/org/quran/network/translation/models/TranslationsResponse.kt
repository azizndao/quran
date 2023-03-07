package org.quran.network.translation.models

import kotlinx.serialization.Serializable

@Serializable
internal data class TranslationsResponse(
  val translations: List<ApiTranslation>
)
