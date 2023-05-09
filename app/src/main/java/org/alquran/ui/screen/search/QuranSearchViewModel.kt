package org.alquran.ui.screen.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.alquran.verses.repository.VerseRepository
import org.quram.common.utils.QuranDisplayData
import org.quram.common.utils.QuranUtils
import org.quran.translation.repositories.TranslationRepository

class QuranSearchViewModel(
  private val verseRepository: VerseRepository,
  private val translationRepository: TranslationRepository,
  private val quranDisplayData: QuranDisplayData,
) : ViewModel() {

  var uiState by mutableStateOf(
    SearchUiState(
      loading = false,
      active = false,
      onActiveChange = ::onActiveChange,
      query = "",
      onQueryChange = ::onQueryChange,
      results = emptyList()
    )
  )

  private var searchJob: Job? = null

  private fun onActiveChange(value: Boolean) {
    uiState = uiState.copy(active = value)
  }

  private fun onQueryChange(query: String) {
    uiState = uiState.copy(query = query, loading = true)
    searchJob?.cancel()
    searchJob = viewModelScope.launch {
      val results = withContext(Dispatchers.IO){
        buildList {
          if (QuranUtils.doesStringContainArabic(query)) addAll(searchInQuran(query))
          addAll(searchTranslations(query))
        }
      }
      uiState = uiState.copy(results = results, loading = false)
    }
  }

  private suspend fun searchInQuran(query: String): List<SearchResult> {
    return verseRepository.search("$query*").map {
      SearchResult(
        it.sura, it.ayah, it.text,
        "Mushaf",
        subtext = quranDisplayData.getAyahString(it.sura, it.ayah),
        type = SearchResultType.QURAN,
      )
    }
  }

  private suspend fun searchTranslations(query: String): List<SearchResult> = buildList {
    for (edition in translationRepository.search("$query*")) {
      edition.verses.forEach { verse ->
        val isArabic = query.all { QuranUtils.doesStringContainArabic(it.toString()) }
        add(
          SearchResult(
            verse.sura,
            verse.ayah,
            verse.text,
            edition.locale.authorName,
            subtext = quranDisplayData.getAyahString(verse.sura, verse.ayah),
            type = when {
              isArabic -> SearchResultType.ARABIC
              else -> SearchResultType.LATIN
            }
          )
        )
      }
    }
  }
}