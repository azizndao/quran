package org.quran.features.pager

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import arg.quran.models.quran.VerseKey
import org.quran.ui.theme.LocalQuranTextStyle
import org.quran.ui.theme.LocalSurahTextStyle
import org.quran.ui.theme.LocalTranslationTextStyle
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import org.quran.core.audio.PlaybackConnection

private const val PARAM_PAGE = "page"
private const val PARAM_SURAH_AYAH = "suraAyah"
const val ROUTE_QURAN_PAGER = "quran/${PARAM_PAGE}s/{page}?ayah={$PARAM_SURAH_AYAH}"

fun directionToQuranPage(page: Int, verseKey: VerseKey? = null): String {
  return "quran/${PARAM_PAGE}s/$page?ayah=${verseKey ?: ""}"
}

internal fun quranPagerDestinationArgs(savedStateHandle: SavedStateHandle): QuranPagerArgs {
  return QuranPagerArgs(
    page = savedStateHandle.get<Int>(PARAM_PAGE)!!,
    verseKey = savedStateHandle.get<String>(PARAM_SURAH_AYAH)
      ?.let(VerseKey.Companion::fromString)
  )
}

fun NavGraphBuilder.quranPagerDestination(
  playbackConnection: PlaybackConnection,
  popBackStack: () -> Unit,
  navigateToSearch: () -> Unit,
  navigateToTranslations: () -> Unit,
  navigateToShare: (verse: VerseKey) -> Unit,
) {
  composable(
    route = ROUTE_QURAN_PAGER,
    arguments = listOf(
      navArgument(PARAM_PAGE) { type = NavType.IntType },
      navArgument(PARAM_SURAH_AYAH) { type = NavType.StringType; nullable = true }
    ),
  ) {
    val viewModel = getViewModel<QuranPagerViewModel> { parametersOf(playbackConnection) }

    CompositionLocalProvider(
      LocalQuranTextStyle provides LocalQuranTextStyle.current.copy(fontSize = 28.sp),
      LocalTranslationTextStyle provides LocalTranslationTextStyle.current.copy(fontSize = 14.sp),
      LocalSurahTextStyle provides LocalSurahTextStyle.current.copy()
    ) {
      QuranPagerScreen(
        viewModel = viewModel,
        popBackStack = popBackStack,
        pageProvider = { mode, page, version -> viewModel.pageFactory(mode, page, version) },
        navigateToSearch = navigateToSearch,
        navigateToTranslations = navigateToTranslations,
        navigateToShare = navigateToShare
      )
    }
  }
}