package org.alquran.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import arg.quran.models.audio.Qari
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import org.quran.core.audio.PlaybackConnection
import org.quran.features.audio.AudioBottomBar
import org.quran.features.audio.AudioMenuSheet
import org.quran.features.audio.AudioViewModel
import org.quran.features.home.HomeQuranRoute
import org.quran.features.pager.PagerRoute
import org.quran.features.reciter.navigation.navigateToReciter


@Composable
fun QuranApp(
  playbackConnection: PlaybackConnection,
  navController: NavHostController = rememberNavController(),
  audioViewModel: AudioViewModel = getViewModel { parametersOf(playbackConnection) }
) {
  var visible by remember { mutableStateOf(true) }

  Scaffold(
    contentWindowInsets = WindowInsets.navigationBars,
    bottomBar = {
      LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collectLatest { entry ->
          visible = entry.destination.route in listOf(HomeQuranRoute, PagerRoute)
        }
      }
      AudioBottomBar(visible = visible, viewModel = audioViewModel, navigateToReciter = navController::navigateToReciter)
    },
  ) { innerPadding ->
    NabGraph(
      contentPadding = innerPadding,
      playbackConnection = playbackConnection,
      navController = navController,
      onFullscreen = { fullscreen -> visible = !fullscreen }
    )
  }
}


@Composable
fun AudioBottomBar(
  viewModel: AudioViewModel,
  visible: Boolean = true,
  navigateToReciter: (Qari) -> Unit = {}
) {

  val playing by viewModel.playingStateFlow.collectAsStateWithLifecycle()

  val showBottomBar by remember(visible) { derivedStateOf { visible && playing != null } }

  AnimatedVisibility(
    visible = showBottomBar,
    enter = slideInVertically { it },
    exit = slideOutVertically { it },
  ) {
    var sowBottomSheet by remember { mutableStateOf(false) }

    AudioBottomBar(uiState = playing!!, onEvent = viewModel::onAudioEvent) {
      sowBottomSheet = true
    }

    if (sowBottomSheet) {
      val audioUiState by viewModel.audioStateFlow.collectAsStateWithLifecycle()

      AudioMenuSheet(
        audioUiState = audioUiState,
        onEvent = viewModel::onAudioEvent,
        onDismissRequest = { sowBottomSheet = false },
        showReciter = navigateToReciter
      )
    }
  }
}