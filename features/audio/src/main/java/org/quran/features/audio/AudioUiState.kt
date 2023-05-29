package org.quran.features.audio

import androidx.media3.common.Player
import arg.quran.models.audio.Qari
import org.quran.core.audio.models.AudioState
import org.quran.core.audio.models.NowPlaying

data class AudioUiState(
  val loading: Boolean = true,
  val playing: NowPlaying? = null,
  val qaris: List<Qari> = emptyList(),
  val audioState: AudioState = AudioState(),
  val currentReciterId: String = "",
  @Player.RepeatMode val repeatMode: Int = Player.REPEAT_MODE_OFF,
)