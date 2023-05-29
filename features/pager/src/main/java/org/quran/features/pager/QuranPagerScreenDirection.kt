package org.quran.features.pager

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import arg.quran.models.quran.VerseKey
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import org.quran.core.audio.PlaybackConnection
import org.quran.ui.theme.LocalQuranTextStyle
import org.quran.ui.theme.LocalSurahTextStyle
import org.quran.ui.theme.LocalTranslationTextStyle

private const val PARAM_PAGE = "page"
private const val PARAM_VERSE = "verse"

fun NavController.navigateToPage(page: Int, verseKey: VerseKey? = null) {
  return navigate("quran/$page?ayah=${verseKey ?: ""}") {
    launchSingleTop = true
  }
}

internal fun getInitialPage(savedStateHandle: SavedStateHandle) = savedStateHandle.get<Int>(PARAM_PAGE)!!

internal fun getInitialVerse(savedStateHandle: SavedStateHandle) = savedStateHandle
  .get<String>(PARAM_VERSE)?.let(VerseKey.Companion::fromString)

fun NavGraphBuilder.quranPagerDestination(
  playbackConnection: PlaybackConnection,
  popBackStack: () -> Unit,
  navigateToSearch: () -> Unit,
  navigateToTranslations: () -> Unit,
  navigateToShare: (verse: VerseKey) -> Unit,
) {
  composable(
    route = "quran/{${PARAM_PAGE}}?ayah={$PARAM_VERSE}",
    arguments = listOf(
      navArgument(PARAM_PAGE) { type = NavType.IntType },
      navArgument(PARAM_VERSE) { type = NavType.StringType; nullable = true }
    ),
  ) {
    val viewModel = getViewModel<QuranPagerViewModel> { parametersOf(playbackConnection) }

    CompositionLocalProvider(
      LocalQuranTextStyle provides LocalQuranTextStyle.current.copy(fontSize = 28.sp),
      LocalTranslationTextStyle provides LocalTranslationTextStyle.current.copy(fontSize = 14.sp),
      LocalSurahTextStyle provides LocalSurahTextStyle.current
    ) {
      QuranPagerScreen(
        viewModel = viewModel,
        popBackStack = popBackStack,
        navigateToSearch = navigateToSearch,
        navigateToTranslations = navigateToTranslations,
        navigateToShare = navigateToShare
      )
    }
  }
}