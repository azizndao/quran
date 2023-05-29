package org.quran.features.audio

import arg.quran.models.audio.Qari
import arg.quran.models.quran.VerseKey

sealed class AudioEvent {
  class ChangeReciter(val reciter: Qari) : AudioEvent()

  class Play(val reciter: String, val verse: VerseKey) : AudioEvent()

  object PlayOrPause : AudioEvent()

  object SkipToNext : AudioEvent()

  object SkipToPrevious : AudioEvent()

  object SetRepeatMode : AudioEvent()

  class PositionChanged(val position: Long) : AudioEvent()
}