package org.quran.features.reciter

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.quram.common.repositories.SurahRepository
import org.quran.core.audio.models.MediaId
import org.quran.core.audio.repositories.QariRepository
import org.quran.core.audio.repositories.RecitationRepository
import timber.log.Timber

@SuppressLint("UnsafeOptInUsageError")
class QariViewModel(
  slug: String,
  surahRepository: SurahRepository,
  private val recitationRepository: RecitationRepository,
  qariRepository: QariRepository,
) : ViewModel() {


  val uiStateFlow = combine(
    flowOf(qariRepository.getQari(slug)),
    flowOf(surahRepository.getAllSurahs()),
    recitationRepository.getDownloadedRecitations(slug)
  ) { qari, surahs, downloadedMediaItems ->

    QariUiState(
      loading = false,
      reciter = qari,
      recitations = surahs.map { sura ->
        val download = downloadedMediaItems.find {
          MediaId.fromString(it.request.id).sura == sura.number
        }
        SurahUiState(surah = sura, download = download)
      }.toPersistentList()
    )
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), QariUiState())


  fun download(recitation: SurahUiState) {
    viewModelScope.launch {
      val reciter = uiStateFlow.value.reciter!!
      val surah = recitation.surah
      Timber.tag("RecitationViewModel")
        .i("Downloading surah ${surah.nameSimple}, reciter = ${reciter.databaseName} (${reciter.id})")
      recitationRepository.downloadRecitation(reciter, surah.number)
    }
  }
}