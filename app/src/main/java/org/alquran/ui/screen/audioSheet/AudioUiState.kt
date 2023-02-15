package org.alquran.ui.screen.audioSheet

import androidx.media3.common.Player
import org.alquran.audio.models.AudioState
import org.alquran.audio.models.NowPlaying
import org.alquran.audio.models.Reciter

data class AudioUiState(
    val loading: Boolean = true,
    val reciters: List<Reciter> = emptyList(),
    val playing: NowPlaying? = null,
    val audioState: AudioState = AudioState.PAUSED,
    val currentReciterId: String = "",
    @Player.RepeatMode val repeatMode: Int = Player.REPEAT_MODE_OFF,
)

data class PlaylistUiState(
    val loading: Boolean = true,
    val playingIndex : Int = 0,
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