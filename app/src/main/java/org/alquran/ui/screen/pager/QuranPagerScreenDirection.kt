package org.alquran.ui.screen.pager

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import arg.quran.models.quran.VerseKey
import org.alquran.ui.theme.LocalQuranTextStyle
import org.alquran.ui.theme.LocalSurahTextStyle
import org.alquran.ui.theme.LocalTranslationTextStyle
import org.koin.androidx.compose.getViewModel

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
) {
  composable(
    route = ROUTE_QURAN_PAGER,
    arguments = listOf(
      navArgument(PARAM_PAGE) { type = NavType.IntType },
      navArgument(PARAM_SURAH_AYAH) { type = NavType.StringType; nullable = true }
    ),
  ) {
    val viewModel = getViewModel<QuranPagerViewModel>()

    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    CompositionLocalProvider(
      LocalQuranTextStyle provides LocalQuranTextStyle.current.copy(fontSize = 28.sp),
      LocalTranslationTextStyle provides LocalTranslationTextStyle.current.copy(fontSize = 14.sp),
      LocalSurahTextStyle provides LocalSurahTextStyle.current.copy()
    ) {
      QuranPagerScreen(
        uiState = uiState,
        audioStateFlow = viewModel.nowPlayingFlow,
        popBackStack = popBackStack,
        navigate = navigate,
        pageProvider = { mode, page, version -> viewModel.pageFactory(mode, page, version) },
      )
    }
  }
}