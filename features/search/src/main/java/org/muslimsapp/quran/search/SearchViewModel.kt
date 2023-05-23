package org.muslimsapp.quran.search

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.alquran.verses.repository.VerseRepository
import org.quram.common.core.QuranInfo
import org.quram.common.utils.QuranDisplayData
import org.quran.translation.repositories.TranslationsRepository
import org.quran.translation.repositories.VerseTranslationRepository

internal class SearchViewModel(
  private val ayahRepository: VerseRepository,
  private val quranInfo: QuranInfo,
  private val quranDisplayData: QuranDisplayData,
  app: Application,
  private val verseTranslationRepository: VerseTranslationRepository,
  private val translationRepository: TranslationsRepository,
) : AndroidViewModel(app) {

  private var job: Job? = null

  var uiState by mutableStateOf(
    SearchUiState(
      onQueryChange = ::updateQuery,
      getPage = ::getPage,
      onActiveChange = ::onActiveChange,
      setSelectedEdition = ::setSelectedEdition
    )
  )
    private set

  fun refresh() {
    viewModelScope.launch {
      val translationEditions = translationRepository.observeSelectedEditions().first()
      uiState = uiState.copy(translations = translationEditions.toPersistentList())
    }
  }

  private fun updateQuery(value: String) {
    uiState = uiState.copy(query = value, loading = true)
    searchAyah()
  }

  private fun searchAyah() {
    job?.cancel()
    job = viewModelScope.launch {
      val q = "${uiState.query}*"

      val results = withContext(Dispatchers.IO) { searchResults(uiState.selectedEdition, q) }

      uiState = uiState.copy(loading = false, results = results.toPersistentList())
    }
  }

  private fun getPage(item: SearchResult) = quranInfo.getPageFromSuraAyah(item.sura, item.ayah)

  private fun onActiveChange(active: Boolean) {
    uiState = uiState.copy(active = active)
  }

  private fun setSelectedEdition(slug: String?) {
    uiState = uiState.copy(selectedEdition = slug)
    searchAyah()
  }

  private suspend fun searchResults(
    selectedEdition: String?,
    q: String
  ) = if (selectedEdition == null) {
    ayahRepository.search(query = q).map {
      SearchResult(
        sura = it.sura,
        ayah = it.ayah,
        text = it.text,
        type = SearchResult.Type.QURAN,
        name = quranDisplayData.getAyahString(it.sura, it.ayah)
      )
    }
  } else {
    verseTranslationRepository.search(selectedEdition, q).map {
      SearchResult(
        sura = it.sura,
        ayah = it.ayah,
        text = it.text,
        type = SearchResult.Type.TRANSLATION,
        name = quranDisplayData.getAyahString(it.sura, it.ayah)
      )
    }
  }
}