package org.alquran.ui.screen.audioSheet

import org.alquran.audio.models.Reciter


internal sealed class AudioEvent {
    object SkipPrevious : AudioEvent()

    object SkipNext : AudioEvent()

    class Play(val reciter: String, val sura: Int, val aya: Int = 1) : AudioEvent()

    object PlayOrPause : AudioEvent()

    object SetRepeatMode : AudioEvent()

    class ChangeReciter(val reciter: Reciter) : AudioEvent()
}
