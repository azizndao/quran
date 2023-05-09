package org.quran.translation.api.models

import arg.quran.models.quran.Verse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ApiTranslatedVerse(
  @SerialName("chapter_id") override val sura: Int,
  @SerialName("verse_number") override val ayah: Int,
  override val text: String
): Verse()
