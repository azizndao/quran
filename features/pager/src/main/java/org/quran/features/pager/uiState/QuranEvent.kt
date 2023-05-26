package org.quran.features.pager.uiState

import arg.quran.models.quran.VerseKey
import org.quran.datastore.DisplayMode


sealed class QuranEvent {
  class Play(val key: VerseKey) : QuranEvent()

  class ToggleBookmark(val verse: VerseKey, val isBookmark: Boolean) : QuranEvent()

  class Selection(val selection: QuranSelection) : QuranEvent()

  class AyahLongPressed(val key: VerseKey, val word: Int?, val bookmarked: Boolean) :
    QuranEvent()

  object AyahPressed : QuranEvent()

  object PlayOrPause : QuranEvent()

  object SkipToPrevious : QuranEvent()

  object SkipToNext : QuranEvent()

  class VerseTafsir(val key: VerseKey) : QuranEvent()

  class VerseNote(val key: VerseKey) : QuranEvent()
}

internal interface QuranEventListener {
  fun onPlay(verseKey: VerseKey)

  fun onPlayOrPause()

  fun onSkipToNext()

  fun onSkipToPrevious()

  fun onAyahLongPressed(verseKey: VerseKey, word: Int?, bookmarked: Boolean)
  fun onAyahPressed()

  fun onBookmark(verseKey: VerseKey, bookmarked: Boolean)

  fun onSelection(selection: QuranSelection)

  fun onChangeDisplayMode(mode: DisplayMode)
}