package org.quran.network.translation.models

import arg.quran.models.Language
import kotlinx.serialization.Serializable

@Serializable
internal data class LanguagesResponse(
  val languages: List<Language>
)