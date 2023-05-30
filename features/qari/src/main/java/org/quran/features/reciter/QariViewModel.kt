package org.quran.features.reciter

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.quram.common.repositories.SurahRepository
import org.quran.core.audio.models.MediaId
import org.quran.core.audio.repositories.QariRepository
import org.quran.core.audio.repositories.RecitationRepository
import timber.log.Timber

@SuppressLint("UnsafeOptInUsageError")
class QariViewModel(
  private val slug: String,
  surahRepository: SurahRepository,
  private val recitationRepository: RecitationRepository,
  private val qariRepository: QariRepository,
) : ViewModel() {

  var uiState by mutableStateOf(QariUiState())
    private set

  private var job: Job? = null

  private val dataFlow = combine(
    flowOf(surahRepository.getAllSurahs()),
    recitationRepository.getDownloadedRecitations(slug)
  ) { surahs, downloadedMediaItems ->

    surahs.map { surah ->
      val download = downloadedMediaItems.find {
        MediaId.fromString(it.request.id).sura == surah.number
      }
      SurahUiState(surah = surah, download = download)
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
        uiState = QariUiState(
          loading = false,
          reciter = qariRepository.getQari(slug),
          recitations = recitations.toImmutableList()
        )
      }
    }
  }

  fun download(recitation: SurahUiState) {
    viewModelScope.launch {
      val reciter = uiState.reciter!!
      val surah = recitation.surah
      Timber.tag("RecitationViewModel")
        .i("Downloading surah ${surah.nameSimple}, reciter = ${reciter.databaseName} (${reciter.id})")
      recitationRepository.downloadRecitation(reciter, surah)
    }
  }
}