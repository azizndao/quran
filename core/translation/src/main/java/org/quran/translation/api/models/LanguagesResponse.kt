package org.quran.translation.api.models

import arg.quran.models.Language
import kotlinx.serialization.Serializable

@Serializable
internal data class LanguagesResponse(
  val languages: List<Language>
)