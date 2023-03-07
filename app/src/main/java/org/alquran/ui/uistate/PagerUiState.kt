package org.alquran.ui.uistate

import org.quran.datastore.DisplayMode
import org.quran.datastore.FontScale

data class PagerUiState(
  val initialPage: Int,
  val playingPage: Int? = null,
  val displayMode: DisplayMode,
  val quranFontScale: FontScale,
  val translationFontScale: FontScale,
  val version: Int,
)
