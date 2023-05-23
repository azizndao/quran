package org.quran.translation.api.model

import kotlinx.serialization.Serializable
import org.quran.translation.local.model.Language

@Serializable
internal data class LanguagesResponse(
  val languages: List<Language>
)