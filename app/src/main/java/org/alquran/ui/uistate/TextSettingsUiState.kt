package org.alquran.ui.uistate

import org.quran.datastore.DisplayMode
import org.quran.datastore.QuranEdition
import org.quran.datastore.TranslationEdition


data class TextSettingsUiState(
  val loading: Boolean = true,
  val data: TextSettings? = null
)

data class TextSettings(
  val quran: QuranEdition,
  val translation: TranslationEdition?,
  val translationEnabled: Boolean,
  val transliteration: TranslationEdition?,
  val transliterationEnabled: Boolean,
  val displayMode: DisplayMode,
  val quranFontFamily: Int,
  val quranFontSize: Float,
  val translationFontSize: Float,
)