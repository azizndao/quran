package org.alquran.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import org.muslimsapp.quran.search.navigateToSearch
import org.muslimsapp.quran.search.searchDestination
import org.muslimsapp.quran.translations.navigateToTranslations
import org.muslimsapp.quran.translations.translationDestination
import org.quran.core.audio.PlaybackConnection
import org.quran.features.home.HomeQuranRoute
import org.quran.features.home.homeDestination
import org.quran.features.pager.navigateToPage
import org.quran.features.pager.quranPagerDestination
import org.quran.features.settings.navigateToSettings
import org.quran.features.settings.settingsDirection
import org.quran.features.share.navigation.navigateToShareAyah
import org.quran.features.share.navigation.shareAyahDestination


@Composable
fun NabGraph(
  contentPadding: PaddingValues = PaddingValues(),
  playbackConnection: PlaybackConnection,
  navController: NavHostController,
  onFullscreen: (Boolean) -> Unit,
) {
  NavHost(
    navController = navController,
    startDestination = HomeQuranRoute,
  ) {
    homeDestination(
      contentPadding = contentPadding,
      navigateToMore = navController::navigateToSettings,
      navigateToSearch = navController::navigateToSearch,
      navigateToPage = navController::navigateToPage
    )

    searchDestination(
      popBackStack = navController::popBackStack,
      navigateToPage = navController::navigateToPage
    )

    quranPagerDestination(
      playbackConnection = playbackConnection,
      popBackStack = navController::popBackStack,
      navigateToSearch = navController::navigateToSearch,
      navigateToTranslations = navController::navigateToTranslations,
      navigateToShare = navController::navigateToShareAyah,
      onFullscreen = onFullscreen
    )

    translationDestination(navController::popBackStack)

    shareAyahDestination(navController::popBackStack)

    settingsDirection(popBackStack = navController::popBackStack)
  }
}

