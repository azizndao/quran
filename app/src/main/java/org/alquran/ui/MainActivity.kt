package org.alquran.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import org.alquran.ui.navigation.ROUTE_RECITERS
import org.alquran.ui.navigation.ROUTE_RECITER_RECITATIONS
import org.alquran.ui.navigation.audioNavGraph
import org.alquran.ui.navigation.directionToReciterRecitation
import org.alquran.ui.screen.audioSheet.PlaybackBottomSheet
import org.alquran.ui.screen.audioSheet.PlaybackSheetViewModel
import org.alquran.ui.screen.home.ROUTE_QURAN_HOME
import org.alquran.ui.screen.home.homeDestination
import org.alquran.ui.screen.pager.ROUTE_QURAN_PAGER
import org.alquran.ui.screen.pager.quranPagerDestination
import org.alquran.ui.theme.QuranFontFamilies
import org.alquran.ui.theme.QuranTheme
import org.alquran.ui.theme.bottomSheet
import org.koin.android.ext.android.inject
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.scope.activityRetainedScope
import org.koin.core.scope.Scope
import org.muslimapp.core.audio.PlaybackConnection
import org.muslimapp.feature.quran.ui.LocalInsetsPadding

class MainActivity : ComponentActivity(), AndroidScopeComponent {

    override val scope: Scope by activityRetainedScope()
    private val playbackConnection by inject<PlaybackConnection>()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        playbackConnection.connect()
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

val MAIN_DESTINATIONS = listOf(
    ROUTE_QURAN_HOME,
    ROUTE_QURAN_PAGER,
    ROUTE_RECITERS,
    ROUTE_RECITER_RECITATIONS,
)

@Composable
fun QuranApp(
    bottomSheetNavigator: BottomSheetNavigator = rememberBottomSheetNavigator(),
    navController: NavHostController = rememberNavController(bottomSheetNavigator),
    playbackViewModel: PlaybackSheetViewModel = getViewModel()
) {
    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        sheetShape = MaterialTheme.shapes.bottomSheet,
        sheetBackgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
    ) {
        var (isFullscreen, setFullScreen) = remember { mutableStateOf(false) }

        val fullscreenTransition = updateTransition(
            targetState = isFullscreen,
            label = "Fullscreen"
        )

        LaunchedEffect(Unit) {
            navController.currentBackStackEntryFlow
                .distinctUntilChangedBy { it.destination.route }
                .collectLatest {
                    isFullscreen = it.destination.route !in MAIN_DESTINATIONS
                }
        }

        PlaybackBottomSheet(
            viewModel = playbackViewModel,
            isFullscreen = fullscreenTransition,
            onPositionChange = playbackViewModel::onPositionChange,
            showReciter = { navController.navigate(directionToReciterRecitation(it.id)) },
        ) { innerPadding ->
            val layoutDirection = LocalLayoutDirection.current
            val windowInsets = WindowInsets.navigationBars.add(
                WindowInsets(
                    bottom = innerPadding.calculateBottomPadding(),
                    top = 0.dp,
                    left = innerPadding.calculateLeftPadding(layoutDirection),
                    right = innerPadding.calculateRightPadding(layoutDirection)
                )
            )
            CompositionLocalProvider(LocalInsetsPadding provides windowInsets) {
                NavHost(navController = navController, startDestination = ROUTE_QURAN_HOME) {
                    homeDestination(navigate = navController::navigate) {}
                    audioNavGraph(
                        navigate = navController::navigate,
                        popBackStack = navController::popBackStack
                    )

                    quranPagerDestination(
                        navController::navigate,
                        navController::popBackStack,
                        fullscreenTransition,
                        setFullScreen
                    )
                }
            }
        }
    }
}