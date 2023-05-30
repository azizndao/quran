package org.quran.features.pager.uiState

import arg.quran.models.audio.Qari
import arg.quran.models.quran.VerseKey


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

  class ChangeReciter(val qari: Qari) : QuranEvent()
}