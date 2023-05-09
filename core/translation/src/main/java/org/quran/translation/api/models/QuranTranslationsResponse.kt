package org.quran.translation.api.models

import kotlinx.serialization.Serializable

@Serializable
internal data class QuranTranslationsResponse(
  val translations: List<ApiTranslatedVerse>
)
