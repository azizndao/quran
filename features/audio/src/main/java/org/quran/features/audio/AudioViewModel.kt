package org.quran.features.audio

import android.app.Application
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.quram.common.repositories.SurahRepository
import org.quran.core.audio.PlaybackConnection
import org.quran.core.audio.models.NowPlaying
import org.quran.core.audio.repositories.QariRepository
import org.quran.datastore.repositories.AudioPreferencesRepository

class AudioViewModel(
  private val qariRepository: QariRepository,
  private val audioSettings: AudioPreferencesRepository,
  private val playbackConnection: PlaybackConnection,
  private val surahRepository: SurahRepository,
  app: Application,
) : AndroidViewModel(app) {

  val playingStateFlow = playbackConnection.isConnected.flatMapMerge {
    if (it) playbackConnection.nowPlaying else flowOf(null)
  }.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5_000),
    null
  )

  val audioStateFlow: StateFlow<AudioUiState> = combine(
    audioSettings.getCurrentReciter(),
    playbackConnection.playingState,
    playingStateFlow,
    playbackConnection.repeatMode,
    flowOf(qariRepository.getQariList())
  ) { currentReciterId, isPlaying, mediaItem, repeatMode, qaris ->

    AudioUiState(
      loading = false,
      qaris = qaris.toPersistentList(),
      playing = mediaItem ?: emptyPlayback(currentReciterId),
      currentReciterId = currentReciterId,
      audioState = isPlaying,
      repeatMode = repeatMode
    )
  }.stateIn(viewModelScope, SharingStarted.Eagerly, AudioUiState())

  private suspend fun emptyPlayback(currentReciterId: String): NowPlaying {
    val position = audioSettings.getPlaybackHistory().first()
    val reciter = qariRepository.getQariItem(currentReciterId)
    val surah = surahRepository.getSurah(position.surah)

    return NowPlaying(
      title = surah.nameSimple,
      reciterName = reciter.name,
      sura = position.surah,
      ayah = position.ayah,
      reciter = currentReciterId,
      artWork = reciter.imageUri?.toUri(),
    )
  }

  fun onAudioEvent(event: AudioEvent) {
    viewModelScope.launch {
      when (event) {
        AudioEvent.PlayOrPause -> playbackConnection.playOrPause()
        AudioEvent.SkipToNext -> playbackConnection.skipToNextSurah()
        AudioEvent.SkipToPrevious -> playbackConnection.skipToPreviousSurah()
        AudioEvent.SetRepeatMode -> playbackConnection.setRepeatMode()
        is AudioEvent.ChangeReciter -> audioSettings.setCurrentReciter(event.reciter.slug)
        is AudioEvent.PositionChanged -> playbackConnection.seekToPosition(event.position)
        is AudioEvent.Play -> playbackConnection.onPlaySurah(
          reciterId = event.reciter,
          sura = event.verse.sura,
          startAya = event.verse.aya
        )
      }
    }
  }
}