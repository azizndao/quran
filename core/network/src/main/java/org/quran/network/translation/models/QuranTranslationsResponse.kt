package org.quran.network.translation.models

import arg.quran.models.quran.VerseTranslation
import kotlinx.serialization.Serializable

@Serializable
internal data class QuranTranslationsResponse(
  val translations: List<VerseTranslation>
)
