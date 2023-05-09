package org.alquran.ui.screen.pager

import arg.quran.models.quran.VerseKey
import org.alquran.ui.uistate.QuranSelection
import org.quran.datastore.DisplayMode


sealed class QuranEvent {
  class Play(val verseKey: VerseKey) : QuranEvent()

  class ToggleBookmark(val verseKey: VerseKey, val isBookmark: Boolean) : QuranEvent()

  class UpdateSelection(val sura: Int, val ayah: Int, val selected: Boolean) : QuranEvent()

  class Selection(val selection: QuranSelection) : QuranEvent()

  class AyahLongPressed(val verseKey: VerseKey, val word: Int?, val bookmarked: Boolean) :
    QuranEvent()

  object AyahPressed : QuranEvent()

  class ChangeDisplayMode(val mode: DisplayMode) : QuranEvent()

  class FullscreenChange(val value: Boolean) : QuranEvent()
  object PlayOrPause : QuranEvent()

  object SkipToPrevious : QuranEvent()

  object SkipToNext : QuranEvent()
}
