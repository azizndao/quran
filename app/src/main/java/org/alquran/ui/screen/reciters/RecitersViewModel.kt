package org.alquran.ui.screen.reciters

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.alquran.audio.models.Reciter
import org.muslimapp.core.audio.repositories.RecitationRepository

internal class RecitersViewModel(
    private val reciterRepository: RecitationRepository,
) : ViewModel() {

    var uiState by mutableStateOf(emptyList<Reciter>())
        private set

    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            uiState = reciterRepository.getAllReciters()
        }
    }
}