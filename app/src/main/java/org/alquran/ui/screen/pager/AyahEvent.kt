package org.alquran.ui.screen.pager

import org.quram.common.model.VerseKey

sealed class AyahEvent {
    class Play(val verseKey: VerseKey) : AyahEvent()

    class ToggleBookmark(val verseKey: VerseKey, val isBookmark: Boolean) : AyahEvent()

    class UpdateSelection(val sura: Int, val ayah: Int, val selected: Boolean) : AyahEvent()

    class AyahLongPressed(val verseKey: VerseKey) : AyahEvent()

    class AyahPressed(val verseKey: VerseKey?) : AyahEvent()
}
