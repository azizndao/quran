package org.quran.translation.local.models

import arg.quran.models.quran.Verse
import org.quran.datastore.LocaleTranslation

data class TranslatedEdition(
  val locale: LocaleTranslation,
  val verses: List<Verse>
)