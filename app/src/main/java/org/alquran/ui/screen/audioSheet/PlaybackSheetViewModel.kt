package org.alquran.ui.screen.audioSheet

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.muslimapp.core.audio.PlaybackConnection
import org.muslimapp.core.audio.repositories.QariRepository
import org.quran.datastore.repositories.AudioPreferencesRepository

class PlaybackSheetViewModel(
  private val audioSettings: AudioPreferencesRepository,
  private val playbackConnection: PlaybackConnection,
  private val qariRepository: QariRepository,
  app: Application,
) : AndroidViewModel(app) {

  private val nowPlaying =
    playbackConnection.nowPlaying.stateIn(viewModelScope, SharingStarted.Lazily, null)

  val audioStateFlow: StateFlow<AudioUiState> = combine(
    audioSettings.getCurrentReciter(),
    nowPlaying,
    playbackConnection.repeatMode,
  ) { currentReciterId, mediaItem, repeatMode ->

    AudioUiState(
      loading = false,
      qaris = qariRepository.getQariList(),
      playing = mediaItem,
      currentReciterId = currentReciterId,
      repeatMode = repeatMode,
      onAudioEvent = ::onAudioEvent
    )
  }.stateIn(viewModelScope, SharingStarted.Eagerly, AudioUiState())

  internal fun onAudioEvent(event: AudioEvent) {
    viewModelScope.launch {
      when (event) {
        AudioEvent.PlayOrPause -> playbackConnection.playOrPause()
        AudioEvent.SkipNext -> playbackConnection.skipToNextSurah()
        AudioEvent.SkipPrevious -> playbackConnection.skipToPreviousSurah()
        AudioEvent.SetRepeatMode -> playbackConnection.setRepeatMode()
        is AudioEvent.ChangeReciter -> audioSettings.setCurrentReciter(event.reciter.slug)
        is AudioEvent.Play -> playbackConnection.onPlaySurah(
          reciterId = event.reciter,
          sura = event.sura,
          startAya = event.aya
        )
      }
    }
  }
}
