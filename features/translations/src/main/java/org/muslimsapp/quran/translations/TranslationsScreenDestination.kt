package org.muslimsapp.quran.translations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.koin.androidx.compose.getViewModel

fun NavController.navigateToTranslations() = navigate("quran/translations")

fun NavGraphBuilder.translationDestination(popBackStack: () -> Unit) {
  composable("quran/translations") {
    val viewModel: TranslationsViewModel = getViewModel()

    TranslationsScreen(
      uiState = viewModel.uiState,
      popBackStack = popBackStack,
    )
  }
}