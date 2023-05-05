package org.alquran.ui.uistate

import org.quran.datastore.LocaleTranslation


data class TranslationUiState(
  val translation: LocaleTranslation,
  val selected: Boolean,
  val downloaded: Boolean
)