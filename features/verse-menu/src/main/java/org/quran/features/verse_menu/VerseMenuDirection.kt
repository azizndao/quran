package org.quran.features.verse_menu

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import arg.quran.models.quran.VerseKey
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import org.quran.core.audio.PlaybackConnection

private const val PARAM_KEY = "key"
private const val PARAM_WORD = "word"
const val ROUTE_VERSE_MENU = "quran/menu/{$PARAM_KEY}?word={$PARAM_WORD}"

fun directionToVerseMenu(key: VerseKey, word: Int?): String = "quran/menu/$key?word=${word ?: ""}"

internal fun verseMenuDestinationArgs(savedStateHandle: SavedStateHandle): VerseMenuArgs {
  val word = savedStateHandle.get<String>(PARAM_WORD)?.let {
    if (it.isEmpty()) null else it.toInt()
  }
  return VerseMenuArgs(
    key = VerseKey.fromString(savedStateHandle.get<String>(PARAM_KEY)!!)!!,
    word = word,
  )
}


@OptIn(ExperimentalMaterialNavigationApi::class)
 fun NavGraphBuilder.verseMenuDestination(
  playbackConnection: PlaybackConnection,
  popBackStack: () -> Unit,
) {
  bottomSheet(
    route = ROUTE_VERSE_MENU,
    arguments = listOf(
      navArgument(PARAM_KEY) { type = NavType.StringType },
      navArgument(PARAM_WORD) { type = NavType.StringType; nullable = true },
    )
  ) {
    val viewModel = getViewModel<VerseMenuViewModel> { parametersOf(playbackConnection) }
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    if (uiState == null) {
      BottomSheetDefaults.DragHandle()
      LinearProgressIndicator(Modifier.navigationBarsPadding())
    } else {
      VerseMenuSheet(
        uiState = uiState!!,
        popBackStack = popBackStack,
      )
    }
  }
}