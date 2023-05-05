package org.alquran.ui.screen.audioSheet

import androidx.media3.common.Player
import arg.quran.models.audio.Qari
import org.alquran.audio.models.AudioState
import org.alquran.audio.models.NowPlaying

data class AudioUiState(
  val loading: Boolean = true,
  val qaris: List<Qari> = emptyList(),
  val playing: NowPlaying? = null,
  val audioState: AudioState = AudioState.PAUSED,
  val currentReciterId: String = "",
  @Player.RepeatMode val repeatMode: Int = Player.REPEAT_MODE_OFF,
  val onAudioEvent: (AudioEvent) -> Unit = {}
)

data class PlaylistUiState(
  val loading: Boolean = true,
  val playingIndex: Int = 0,
  val items: List<Item> = emptyList()
) {

  data class Item(
    val sura: Int,
    val suraName: String,
    val reciterId: String,
    val reciterName: String,
    val artWork: String,
    val isPlaying: Boolean,
  )
}