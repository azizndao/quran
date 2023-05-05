package org.alquran.ui.uistate

import arg.quran.models.quran.VerseKey
import org.alquran.ui.screen.pager.AyahEvent
import org.quran.datastore.DisplayMode
import org.quran.datastore.FontScale

data class PagerUiState(
  val initialPage: Int,
  val playingPage: Int? = null,
  val displayMode: DisplayMode,
  val quranFontScale: FontScale,
  val translationFontScale: FontScale,
  val version: Int,
  val onDisplayModeChange: (DisplayMode) -> Unit,
  val onAyahEvent: (AyahEvent) -> Unit,
  val isFullscreen: Boolean = false,
  val onFullscreen: (Boolean) -> Unit,
  val selection: Selection = Selection.None,
  val setSelection: (Selection) -> Unit,
)

sealed class Selection {
  data class InitialVerse(val key: VerseKey) : Selection()
  data class Highlight(val key: VerseKey, val isBookmarked: Boolean) : Selection()
  object None : Selection()
}
