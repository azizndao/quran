package org.quran.translation.api.model

import kotlinx.serialization.Serializable
import org.quran.translation.local.model.TranslatedVerse

@Serializable
internal data class QuranTranslationsResponse(
  val translations: List<TranslatedVerse>
)
