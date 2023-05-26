package org.alquran.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import org.muslimsapp.quran.search.directionToSearch
import org.muslimsapp.quran.search.searchDestination
import org.muslimsapp.quran.translations.directionToTranslations
import org.muslimsapp.quran.translations.translationDestination
import org.quran.core.audio.PlaybackConnection
import org.quran.features.home.LocalInsetsPadding
import org.quran.features.home.ROUTE_QURAN_HOME
import org.quran.features.home.homeDestination
import org.quran.features.pager.directionToQuranPage
import org.quran.features.pager.quranPagerDestination
import org.quran.features.saved.savedDestination
import org.quran.features.share.navigation.directionToShareAyah
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
        navigateToSearch = { navController.navigate(directionToSearch()) },
        navigateToPage = { page, key -> navController.navigate(directionToQuranPage(page, key)) }
      )

      savedDestination(
        contentPadding = contentPadding,
        navigateToMore = {},
        navigateToSearch = { navController.navigate(directionToSearch()) },
        navigateToPage = { page, key -> navController.navigate(directionToQuranPage(page, key)) }
      )

      searchDestination(
        popBackStack = navController::popBackStack,
        navigateToPage = { page, key -> navController.navigate(directionToQuranPage(page, key)) }
      )

      quranPagerDestination(
        playbackConnection = playbackConnection,
        popBackStack = navController::popBackStack,
        navigateToSearch = { navController.navigate(directionToSearch()) },
        navigateToTranslations = { navController.navigate(directionToTranslations()) },
        navigateToShare = { verse ->
          navController.navigate(directionToShareAyah(verse.sura, verse.aya))
        },
      )

      translationDestination(navController::popBackStack)

      shareAyahDestination(navController::popBackStack)
    }
  }
}

