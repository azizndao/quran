package org.alquran.ui.screen.translations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.alquran.R
import org.quram.common.extensions.capital
import org.quran.datastore.repositories.QuranPreferencesRepository
import org.quran.translation.repositories.TranslationRepository
import timber.log.Timber
import java.io.IOException
import java.net.SocketException
import java.util.Locale

internal class TranslationsViewModel(
  private val translationRepository: TranslationRepository,
  private val displayDataStore: QuranPreferencesRepository,
  private val app: Application,
) : AndroidViewModel(app) {


  val uiStateFlow = combine(
    displayDataStore.getSelectedTranslationSlugs(),
    translationRepository.getAvailableTranslations()
  ) { selected, translations ->
    val selectedTranslations = translations.filter { it.slug in selected }

    val uiItems = translations.groupBy { it.languageCode }
    val localEditions = buildList {
      for ((code, list) in uiItems) {
        val editions = LocalEditions(
          displayLanguage = Locale(code).displayLanguage.capital(),
          editions = list.sortedBy { it.authorName }.map { it.toUiState(it.slug in selected) }
        )
        add(editions)
      }
    }

    TranslationsListUiState(
      loading = false,
      selectedTranslations = selectedTranslations,
      editions = localEditions.sortedBy { it.displayLanguage },
    )
  }.catch {
    if (it is SocketException || it is IOException) {
      Timber.e(it)
      emit(
        TranslationsListUiState(
          loading = false,
          exception = app.getString(R.string.network_error)
        )
      )
    } else if (it !is CancellationException) {
      Timber.e(it)
      emit(
        TranslationsListUiState(
          loading = false,
          exception = app.getString(R.string.unknown_error)
        )
      )
    }
  }.flowOn(Dispatchers.IO)
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), TranslationsListUiState())


  fun setTranslation(uiModel: TranslationUiState) {
    viewModelScope.launch {
      if (uiModel.selected) {
        displayDataStore.disableTranslation(uiModel.slug)
      } else {
        displayDataStore.enableTranslation(uiModel.slug)
        if (!uiModel.downloaded) {
          translationRepository.downloadQuranTranslation(uiModel.slug)
          displayDataStore.downloadTranslation(uiModel.slug)
        }
      }
    }
  }
}
