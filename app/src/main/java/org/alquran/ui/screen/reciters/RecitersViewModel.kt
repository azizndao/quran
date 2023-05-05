package org.alquran.ui.screen.reciters

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arg.quran.models.audio.Qari
import kotlinx.coroutines.launch
import org.muslimapp.core.audio.repositories.QariRepository

internal class RecitersViewModel(
  private val reciterRepository: QariRepository,
) : ViewModel() {

  var uiState by mutableStateOf(emptyList<Qari>())
    private set

  init {
    refresh()
  }

  private fun refresh() {
    viewModelScope.launch {
      uiState = reciterRepository.getQariList()
    }
  }
}