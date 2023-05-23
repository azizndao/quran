package org.alquran.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import org.alquran.ui.theme.bottomSheet
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
import org.quran.features.verse_menu.directionToVerseMenu
import org.quran.features.verse_menu.verseMenuDestination


@Composable
fun QuranApp(
  playbackConnection: PlaybackConnection,
  bottomSheetNavigator: BottomSheetNavigator = rememberBottomSheetNavigator(),
  navController: NavHostController = rememberNavController(bottomSheetNavigator)
) {
  ModalBottomSheetLayout(
    bottomSheetNavigator = bottomSheetNavigator,
    sheetShape = MaterialTheme.shapes.bottomSheet,
    sheetBackgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
  ) {

    val windowInsets = WindowInsets(0, 0, 0, 0)
    CompositionLocalProvider(LocalInsetsPadding provides windowInsets) {
      NavHost(navController = navController, startDestination = ROUTE_QURAN_HOME) {
        homeDestination(
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
          navigateToVerseMenu = { verse, word ->
            navController.navigate(directionToVerseMenu(verse, word))
          },
          navigateToTranslations = { navController.navigate(directionToTranslations()) },
          navigateToSearch = { navController.navigate(directionToSearch()) }
        )

        verseMenuDestination(
          playbackConnection = playbackConnection,
          popBackStack = navController::popBackStack,
        )

        translationDestination(popBackStack = navController::popBackStack)
      }
    }
  }
}

