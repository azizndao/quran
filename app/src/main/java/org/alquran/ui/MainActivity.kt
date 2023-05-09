package org.alquran.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import org.alquran.ui.navigation.audioNavGraph
import org.alquran.ui.screen.home.ROUTE_QURAN_HOME
import org.alquran.ui.screen.home.homeDestination
import org.alquran.ui.screen.pager.quranPagerDestination
import org.alquran.ui.screen.search.quranSearchDestination
import org.alquran.ui.screen.translations.quranTranslationDestination
import org.alquran.ui.screen.verseMenu.verseMenuDestination
import org.alquran.ui.theme.bottomSheet
import org.koin.android.ext.android.inject
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityRetainedScope
import org.koin.core.scope.Scope
import org.muslimapp.core.audio.PlaybackConnection
import org.muslimapp.feature.quran.ui.LocalInsetsPadding
import org.quran.ui.theme.QuranFontFamilies
import org.quran.ui.theme.QuranTheme

class MainActivity : ComponentActivity(), AndroidScopeComponent {

  override val scope: Scope by activityRetainedScope()
  private val playbackConnection by inject<PlaybackConnection>()

  override fun onCreate(savedInstanceState: Bundle?) {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    super.onCreate(savedInstanceState)
    lifecycle.addObserver(playbackConnection)
    setContent {
      CompositionLocalProvider(
        LocalFontFamilyResolver provides createFontFamilyResolver(
          LocalContext.current, QuranFontFamilies.handler
        ),
      ) {
        QuranTheme {
          Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
          ) {
            QuranApp()
          }
        }
      }
    }
  }
}

@Composable
fun QuranApp(
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
        homeDestination(navigate = navController::navigate) {}

        audioNavGraph(
          navigate = navController::navigate,
          popBackStack = navController::popBackStack
        )

        quranSearchDestination(
          navigate = navController::navigate,
          popBackStack = navController::popBackStack
        )

        quranPagerDestination(
          navigate = navController::navigate,
          popBackStack = navController::popBackStack
        )

        verseMenuDestination(
          navigate = navController::navigate,
          popBackStack = navController::popBackStack,
        )

        quranTranslationDestination(popBackStack = navController::popBackStack)
      }
    }
  }
}