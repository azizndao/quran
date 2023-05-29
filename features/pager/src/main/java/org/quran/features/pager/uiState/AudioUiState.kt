package org.quran.features.pager.uiState

import androidx.media3.common.Player
import org.quran.core.audio.models.AudioState
import org.quran.core.audio.models.NowPlaying

data class AudioUiState(
  val loading: Boolean = true,
  val playing: NowPlaying? = null,
  val audioState: AudioState = AudioState(),
  val currentReciterId: String = "",
  @Player.RepeatMode val repeatMode: Int = Player.REPEAT_MODE_OFF,
)