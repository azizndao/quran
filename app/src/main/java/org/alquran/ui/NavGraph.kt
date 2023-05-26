package org.alquran.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import org.muslimsapp.quran.search.navigateToSearch
import org.muslimsapp.quran.search.searchDestination
import org.muslimsapp.quran.translations.navigateToTranslations
import org.muslimsapp.quran.translations.translationDestination
import org.quran.core.audio.PlaybackConnection
import org.quran.features.home.LocalInsetsPadding
import org.quran.features.home.ROUTE_QURAN_HOME
import org.quran.features.home.homeDestination
import org.quran.features.pager.navigateToPage
import org.quran.features.pager.quranPagerDestination
import org.quran.features.saved.savedDestination
import org.quran.features.share.navigation.navigateToShareAyah
import org.quran.features.share.navigation.shareAyahDestination


@Composable
fun NabGraph(
  contentPadding: PaddingValues = PaddingValues(),
  playbackConnection: PlaybackConnection,
  navController: NavHostController,
) {
  val windowInsets = WindowInsets(0, 0, 0, 0)
  CompositionLocalProvider(LocalInsetsPadding provides windowInsets) {
    NavHost(
      navController = navController,
      startDestination = ROUTE_QURAN_HOME,
    ) {
      homeDestination(
        contentPadding = contentPadding,
        navigateToMore = {},
        navigateToSearch = navController::navigateToSearch,
        navigateToPage = navController::navigateToPage
      )

      savedDestination(
        contentPadding = contentPadding,
        navigateToMore = {},
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
      )

      translationDestination(navController::popBackStack)

      shareAyahDestination(navController::popBackStack)
    }
  }
}

