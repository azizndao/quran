package org.muslimsapp.quran.translations

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.quram.common.extensions.capital
import org.quran.datastore.LocaleTranslation
import org.quran.translation.repositories.TranslationsRepository
import org.quran.translation.repositories.VerseTranslationRepository
import timber.log.Timber
import java.io.IOException
import java.net.SocketException
import java.util.Locale

internal class TranslationsViewModel(
  private val translationRepository: TranslationsRepository,
  private val verseTranslationRepository: VerseTranslationRepository,
  private val app: Application,
) : AndroidViewModel(app) {

  private var job: Job? = null

  var uiState by mutableStateOf(TranslationsListUiState())
    private set

  private val selectionFlow = MutableStateFlow<String?>(null)

  private val translationsFlow = combine(
    translationRepository.observeSelectedTranslationSlugs(),
    translationRepository.observeAvailableTranslations(),
    selectionFlow,
  ) { activeTranslations, editions, selection ->
    val translations = editions.map { translation ->
      translation.toUiState(
        enabled = translation.slug in activeTranslations,
        selected = selection == translation.slug,
        onClick = { setTranslation(translation) },
        onLonClick = { selectionFlow.value = translation.slug }
      )
    }
    val locales = buildList {
      for ((languageCode, items) in translations.groupBy { it.languageCode }) {
        add(
          LocaleSection(
            displayLanguage = Locale(languageCode).displayLanguage.capital(),
            translations = items.sortedBy { it.authorName }
          )
        )
      }
    }
    TranslationsListUiState(
      loading = false,
      selectedTranslation = selection,
      locales = locales.sortedBy { it.displayLanguage }.toPersistentList(),
      downloadedTranslations = translations.filter { it.enabled }
        .sortedBy { activeTranslations.indexOf(it.slug) }
        .toPersistentList(),
      clearSelection = { selectionFlow.value = null },
      delete = { viewModelScope.launch { translationRepository.deleteTranslation(selection!!) } },
      moveUp = { moveUp(selection) },
      moveDown = { moveDown(selection) },
    )
  }

  private fun moveDown(selection: String?) {
    if (selection != null) {
      viewModelScope.launch {
        translationRepository.moveTranslationDown(selection)
      }
    }
  }

  private fun moveUp(selection: String?) {
    if (selection != null) {
      viewModelScope.launch {
        translationRepository.moveTranslationUp(selection)
      }
    }
  }

  init {
    refresh()
  }

  fun refresh(fetch: Boolean = false) {
    job?.cancel()
    job = viewModelScope.launch {
      uiState = uiState.copy(loading = true)
      try {
        if (fetch) translationRepository.downloadTranslations()
        translationsFlow.collect { state ->
          uiState = state
        }
      } catch (e: SocketException) {
        Timber.e(e)
        uiState = TranslationsListUiState(
          loading = false,
          exception = app.getString(org.quran.ui.R.string.network_error)
        )
      } catch (e: IOException) {
        Timber.e(e)
        uiState = TranslationsListUiState(
          loading = false,
          exception = app.getString(org.quran.ui.R.string.network_error)
        )
      }
    }
  }

  private fun setTranslation(uiModel: LocaleTranslation) {
    viewModelScope.launch {
      if (!uiModel.downloaded) {
        verseTranslationRepository.enqueueDownload(uiModel.slug)
      }
    }
  }
}
