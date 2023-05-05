package org.alquran.ui.screen.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.alquran.verses.repository.VerseRepository
import org.quran.translation.repositories.TranslationRepository

class QuranSearchViewModel(
  private val verseRepository: VerseRepository,
  private val translationRepository: TranslationRepository,
) : ViewModel() {

  var uiState by mutableStateOf(
    SearchUiState(
      active = false,
      onActiveChange = ::onActiveChange,
      query = "",
      onQueryChange = ::onQueryChange,
      results = emptyList()
    )
  )

  private fun onActiveChange(value: Boolean) {
    uiState = uiState.copy(active = value)
  }

  private fun onQueryChange(value: String) {
    uiState = uiState.copy(query = value)
    search(value)
  }

  private var searchJob: Job? = null

  private fun search(query: String) {
    searchJob?.cancel()
    searchJob = viewModelScope.launch {

    }
  }
}