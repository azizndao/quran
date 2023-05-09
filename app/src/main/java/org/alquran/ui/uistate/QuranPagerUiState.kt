package org.alquran.ui.uistate

import arg.quran.models.quran.VerseKey
import org.alquran.audio.models.NowPlaying
import org.alquran.ui.screen.pager.QuranEvent
import org.quran.datastore.DisplayMode
import org.quran.datastore.FontScale


data class QuranPagerUiState(
  val page: Int,
  val playingPage: Int? = null,
  val displayMode: DisplayMode,
  val quranFontScale: FontScale,
  val translationFontScale: FontScale,
  val version: Int,
  val isFullscreen: Boolean = false,
  val nowPlaying: NowPlaying? = null,
  val exception: Exception? = null,
  val onEvent: (QuranEvent) -> Unit,
)

sealed class QuranSelection {
  data class InitialVerse(val key: VerseKey) : QuranSelection()
  data class Highlight(val key: VerseKey, val word: Int?, val isBookmarked: Boolean) :
    QuranSelection()

  object None : QuranSelection()
}
