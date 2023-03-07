package org.alquran.ui.screen.recitations

import androidx.media3.exoplayer.offline.Download
import arg.quran.models.Sura
import arg.quran.models.audio.Qari

data class RecitationsUiState(
  val loading: Boolean = true,
  val reciter: Qari? = null,
  val recitations: List<SurahRecitationUiState> = emptyList(),
  val errorMessages: List<Int> = emptyList(),
)

data class SurahRecitationUiState(
  val sura: Sura,
  val download: Download?,
  val isPlaying: Boolean,
)