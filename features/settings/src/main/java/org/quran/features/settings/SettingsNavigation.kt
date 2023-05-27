package org.quran.features.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.koin.androidx.compose.getViewModel

fun NavController.navigateToSettings() = navigate("settings")

fun NavGraphBuilder.settingsDirection(popBackStack: () -> Unit) {
  composable("settings") {
    SettingsScreen(
      viewModel = getViewModel<SettingsViewModel>(),
      popBackStack = popBackStack
    )
  }
}