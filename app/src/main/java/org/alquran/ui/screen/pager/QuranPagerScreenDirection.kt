package org.alquran.ui.screen.pager

import androidx.compose.animation.core.Transition
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.alquran.ui.theme.LocalQuranTextStyle
import org.alquran.ui.theme.LocalSurahTextStyle
import org.alquran.ui.theme.LocalTranslationTextStyle
import org.koin.androidx.compose.getViewModel
import org.quram.common.model.VerseKey

private const val PARAM_PAGE = "page"
private const val PARAM_SURAH_AYAH = "suraAyah"
const val ROUTE_QURAN_PAGER = "quran/${PARAM_PAGE}s/{page}?ayah={$PARAM_SURAH_AYAH}"

internal fun directionToQuranPage(page: Int, verseKey: VerseKey? = null): String {
    return "quran/${PARAM_PAGE}s/$page?ayah=${verseKey ?: ""}"
}

fun quranPagerDestinationArgs(savedStateHandle: SavedStateHandle): QuranPagerArgs {
    return QuranPagerArgs(
        page = savedStateHandle.get<Int>(PARAM_PAGE)!!,
        verseKey = savedStateHandle.get<String>(PARAM_SURAH_AYAH)
            ?.let(VerseKey.Companion::fromString)
    )
}


internal fun NavGraphBuilder.quranPagerDestination(
    navigate: (String) -> Unit,
    popBackStack: () -> Unit,
    fullscreenTransition: Transition<Boolean>,
    onFullscreen: (Boolean) -> Unit,
) {
    composable(
        route = ROUTE_QURAN_PAGER,
        arguments = listOf(
            navArgument(PARAM_PAGE) { type = NavType.IntType },
            navArgument(PARAM_SURAH_AYAH) { type = NavType.StringType; nullable = true }
        ),
    ) {
        val viewModel = getViewModel<QuranPagerViewModel>()

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        CompositionLocalProvider(
            LocalQuranTextStyle provides LocalQuranTextStyle.current.copy(
                fontSize = 28.sp
            ),
            LocalTranslationTextStyle provides LocalTranslationTextStyle.current.copy(
                fontSize = 14.sp
            ),
            LocalSurahTextStyle provides LocalSurahTextStyle.current.copy()
        ) {
            QuranPagerScreen(
                uiState = uiState,
                popBackStack = popBackStack,
                onDisplayChange = viewModel::onDisplayMode,
                onAyahEvent = viewModel::onAyahEvent,
                pageProvider = { mode, pageNumber, verson ->
                    viewModel.pageFactory(
                        mode,
                        pageNumber,
                        verson
                    )
                },
                navigate = navigate,
                fullscreenTransition = fullscreenTransition,
                onFullscreen = onFullscreen
            )
        }
    }
}