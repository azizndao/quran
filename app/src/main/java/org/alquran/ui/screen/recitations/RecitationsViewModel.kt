package org.alquran.ui.screen.recitations

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arg.quran.models.Sura
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.alquran.R
import org.muslimapp.core.audio.PlaybackConnection
import org.muslimapp.core.audio.models.MediaId
import org.muslimapp.core.audio.repositories.RecitationRepository
import org.quram.common.repositories.SurahRepository
import timber.log.Timber

@SuppressLint("UnsafeOptInUsageError")
class RecitationsViewModel(
  private val reciterId: String,
  surahRepository: SurahRepository,
  private val playbackConnection: PlaybackConnection,
  private val recitationRepository: RecitationRepository,
) : ViewModel() {

//    private val reciterId: String = recitationsArgs(savedStateHandle)

  var uiState by mutableStateOf(RecitationsUiState())
    private set

  private var job: Job? = null

  private val dataFlow = combine(
    flowOf(surahRepository.getAllSurahs()),
    playbackConnection.currentMediaItem,
    recitationRepository.getDownloadedRecitations(reciterId)
  ) { surahs, currentMediaId, downloadedMediaItems ->

    surahs.map { surah ->
      val download = downloadedMediaItems.find {
        MediaId.fromString(it.request.id).sura == surah.number
      }
      SurahRecitationUiState(
        sura = surah,
        isPlaying = currentMediaId?.mediaId?.let { MediaId.fromString(it).sura } == surah.number,
        download = download
      )
    }
  }

  init {
    refresh()
  }

  private fun refresh() {
    job?.cancel()
    uiState = uiState.copy(loading = true)
    job = viewModelScope.launch {
      dataFlow.collect { recitations ->
        uiState = RecitationsUiState(
          loading = false,
          reciter = recitationRepository.getReciter(reciterId),
          recitations = recitations
        )
      }
    }
  }

  fun play() {
    viewModelScope.launch {
      try {
        playbackConnection.onPlaySurah(sura = 1, reciterId = uiState.reciter!!.slug)
      } catch (e: Exception) {
        uiState = uiState.copy(
          errorMessages = listOf(R.string.network_error)
        )
      }
    }
  }

  fun playFromSurah(sura: Sura) {
    viewModelScope.launch {
      try {
        playbackConnection.onPlaySurah(surah = sura.number, reciter = uiState.reciter!!)
      } catch (e: Exception) {
        uiState = uiState.copy(
          errorMessages = listOf(R.string.network_error)
        )
      }
    }
  }

  fun download(recitation: SurahRecitationUiState) {
    viewModelScope.launch {
      val reciter = uiState.reciter!!
      val surah = recitation.sura
      Timber.tag("RecitationViewModel")
        .i("Downloading surah ${surah.nameSimple}, reciter = ${reciter.slug} (${reciter.slug})")
      recitationRepository.downloadRecitation(reciter, surah)
    }
  }
}