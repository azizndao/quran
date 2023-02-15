package org.alquran.ui.screen.audioSheet

import android.app.Application
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.muslimapp.core.audio.PlaybackConnection
import org.alquran.audio.models.AudioState
import org.muslimapp.core.audio.models.MediaId
import org.alquran.audio.models.NowPlaying
import org.muslimapp.core.audio.repositories.RecitationRepository
import org.quram.common.repositories.SurahRepository
import org.quran.datastore.repositories.AudioPreferencesRepository

class PlaybackSheetViewModel (
    private val recitationRepository: RecitationRepository,
    private val audioSettings: AudioPreferencesRepository,
    private val playbackConnection: PlaybackConnection,
    private val surahRepository: SurahRepository,
    app: Application,
) : AndroidViewModel(app) {

    val audioStateFlow: StateFlow<AudioUiState> = combine(
        audioSettings.getCurrentReciter(),
        playbackConnection.playingState,
        playbackConnection.nowPlaying,
        playbackConnection.repeatMode,
    ) { currentReciterId, isPlaying, mediaItem, repeatMode ->

        AudioUiState(
            loading = false,
            reciters = recitationRepository.getAllReciters(),
            playing = mediaItem ?: emptyPlayback(currentReciterId),
            currentReciterId = currentReciterId,
            audioState = isPlaying,
            repeatMode = repeatMode
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, AudioUiState())

    val playlistFlow = playbackConnection.currentMediaItem.map { mediaItem ->
        val mediaId = mediaItem?.mediaId?.let { MediaId.fromString(it) }

        val reciter = recitationRepository.getReciter(
            mediaId?.reciter ?: audioSettings.getPlaybackHistory().first().reciterId
        )

        var playingIndex = 0

        val playlist = surahRepository.getAllSurahs().mapIndexed { index, sura ->
            val isPlaying = mediaId?.sura == sura.number
            if (isPlaying) playingIndex = index
            PlaylistUiState.Item(
                sura = sura.number,
                suraName = sura.nameSimple,
                artWork = reciter.image,
                reciterId = reciter.id,
                reciterName = reciter.name,
                isPlaying = isPlaying
            )
        }

        PlaylistUiState(
            playingIndex = playingIndex,
            loading = false,
            items = playlist,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10_000), PlaylistUiState())

    private suspend fun emptyPlayback(currentReciterId: String): NowPlaying {
        val position = audioSettings.getPlaybackHistory().first()
        val reciter = recitationRepository.getReciter(currentReciterId)
        val surah = surahRepository.getSurah(position.surah)

        return NowPlaying(
            title = surah.nameSimple,
            reciterName = reciter.name,
            sura = position.surah,
            ayah = position.ayah,
            reciter = currentReciterId,
            artWork = reciter.image.toUri(),
            state = AudioState.PAUSED
        )
    }

    internal fun onAudioEvent(event: AudioEvent) {
        viewModelScope.launch {
            when (event) {
                AudioEvent.PlayOrPause -> playbackConnection.playOrPause()
                AudioEvent.SkipNext -> playbackConnection.skipToNextSurah()
                AudioEvent.SkipPrevious -> playbackConnection.skipToPreviousSurah()
                AudioEvent.SetRepeatMode -> playbackConnection.setRepeatMode()
                is AudioEvent.ChangeReciter -> audioSettings.setCurrentReciter(event.reciter.id)
                is AudioEvent.Play -> playbackConnection.onPlaySurah(
                    reciterId = event.reciter,
                    sura = event.sura,
                    startAya = event.aya
                )
            }
        }
    }

    fun onPositionChange(position: Long) {
        playbackConnection.seekToPosition(position)
    }
}
