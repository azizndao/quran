package org.alquran.ui.screen.translations

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.koin.androidx.compose.getViewModel

internal fun directionToQuranTranslations() = "quran/translations"

internal fun NavGraphBuilder.quranTranslationDestination(popBackStack: () -> Unit) {
  composable("quran/translations") {
    val viewModel: TranslationsViewModel = getViewModel()

    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    TranslationsScreen(
      uiState = uiState,
      setTranslation = viewModel::setTranslation,
      popBackStack = popBackStack,
      refresh = { }
    )
  }
}