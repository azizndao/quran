package org.alquran.ui.uistate

import org.quran.datastore.TranslationEdition


data class TranslationUiState(
  val translation: TranslationEdition,
  val selected: Boolean,
  val downloaded: Boolean
)