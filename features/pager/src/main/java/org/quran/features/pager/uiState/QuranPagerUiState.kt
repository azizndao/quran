package org.quran.features.pager.uiState

import androidx.compose.runtime.Stable
import arg.quran.models.quran.VerseKey
import org.quran.datastore.DisplayMode
import org.quran.datastore.FontScale
import org.quran.datastore.serializers.DEFAULT_QURAN_FONT_SIZE
import org.quran.datastore.serializers.DEFAULT_TRANSLATION_FONT_SIZE

@Stable
data class QuranPagerUiState(
  val page: Int,
  val displayMode: DisplayMode = DisplayMode.UNRECOGNIZED,
  val quranFontScale: FontScale = DEFAULT_QURAN_FONT_SIZE,
  val translationFontScale: FontScale = DEFAULT_TRANSLATION_FONT_SIZE,
  val version: Int = 1,
  val exception: Exception? = null,
)

sealed class QuranSelection {
  data class InitialVerse(val key: VerseKey) : QuranSelection()
  data class Highlight(val key: VerseKey, val word: Int?, val isBookmarked: Boolean) :
    QuranSelection()

  object None : QuranSelection()
}
