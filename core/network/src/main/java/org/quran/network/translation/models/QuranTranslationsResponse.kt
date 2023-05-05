package org.quran.network.translation.models

import kotlinx.serialization.Serializable

@Serializable
internal data class QuranTranslationsResponse(
  val translations: List<ApiTranslatedVerse>
)
