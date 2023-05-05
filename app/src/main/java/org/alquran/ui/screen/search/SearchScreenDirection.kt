package org.alquran.ui.screen.search

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.alquran.ui.theme.LocalQuranTextStyle
import org.alquran.ui.theme.LocalSurahTextStyle
import org.alquran.ui.theme.LocalTranslationTextStyle
import org.koin.androidx.compose.getViewModel

private const val PARAM_PAGE = "page"
private const val PARAM_SURAH_AYAH = "suraAyah"
const val ROUTE_QURAN_SEARCH = "quran/search"

internal fun directionToQuranSearch(): String = ROUTE_QURAN_SEARCH

internal fun NavGraphBuilder.quranSearchDestination(
  navigate: (String) -> Unit,
  popBackStack: () -> Unit,
) {
  composable(route = ROUTE_QURAN_SEARCH) {
    val viewModel = getViewModel<QuranSearchViewModel>()
    CompositionLocalProvider(
      LocalQuranTextStyle provides LocalQuranTextStyle.current.copy(fontSize = 28.sp),
      LocalTranslationTextStyle provides LocalTranslationTextStyle.current.copy(fontSize = 14.sp),
      LocalSurahTextStyle provides LocalSurahTextStyle.current.copy()
    ) {
      SearchScreen(
        uiState = viewModel.uiState,
        navigate = navigate,
        popBackStack = popBackStack,
      )
    }
  }
}