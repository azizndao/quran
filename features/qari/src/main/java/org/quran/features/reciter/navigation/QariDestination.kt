package org.quran.features.reciter.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import arg.quran.models.audio.Qari
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import org.quran.features.reciter.QariScreen
import org.quran.features.reciter.QariViewModel

fun NavController.navigateToReciter(qari: Qari) = navigate("reciter/${qari.slug}")

fun NavGraphBuilder.reciterDestination(popBackStack: () -> Unit) {
  composable("reciter/{slug}") {
    val viewModel = getViewModel<QariViewModel> {
      parametersOf(it.arguments!!.getString("slug"))
    }

    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    QariScreen(
      uiState = uiState,
      download = viewModel::download,
      popBackStack = popBackStack
    )
  }
}