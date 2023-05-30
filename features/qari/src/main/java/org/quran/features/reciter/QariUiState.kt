package org.quran.features.reciter

import androidx.media3.exoplayer.offline.Download
import arg.quran.models.Sura
import arg.quran.models.audio.Qari

data class QariUiState(
  val loading: Boolean = true,
  val reciter: Qari? = null,
  val recitations: List<SurahUiState> = emptyList(),
  val errorMessages: List<Int> = emptyList(),
)

data class SurahUiState(
  val surah: Sura,
  val download: Download?,
)