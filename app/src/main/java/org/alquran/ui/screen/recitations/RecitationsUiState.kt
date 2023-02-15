package org.alquran.ui.screen.recitations

import androidx.media3.exoplayer.offline.Download
import org.alquran.audio.models.Reciter
import org.quram.common.model.Sura

data class RecitationsUiState(
    val loading: Boolean = true,
    val reciter: Reciter? = null,
    val recitations: List<SurahRecitationUiState> = emptyList(),
    val errorMessages: List<Int> = emptyList(),
)

data class SurahRecitationUiState(
    val sura: Sura,
    val download: Download?,
    val isPlaying: Boolean,
)