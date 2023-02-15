package org.alquran.ui.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.alquran.ui.screen.recitations.RecitationsScreen
import org.alquran.ui.screen.recitations.RecitationsViewModel
import org.koin.androidx.compose.getViewModel
import org.muslimapp.feature.audio.ui.reciters.RecitersScreen
import org.alquran.ui.screen.reciters.RecitersViewModel
import org.koin.core.parameter.parametersOf

fun directionToReciterRecitation(reciterId: String) = "quran/reciters/$reciterId"

const val ROUTE_RECITERS = "quran/reciters"

private const val PARAM_RECITER_ID = "reciterId"

internal fun recitationsArgs(savedStateHandle: SavedStateHandle): String {
    return savedStateHandle.get<String>(PARAM_RECITER_ID)!!
}

const val ROUTE_RECITER_RECITATIONS = "quran/reciters/{$PARAM_RECITER_ID}"

fun NavGraphBuilder.audioNavGraph(navigate: (String) -> Unit, popBackStack: () -> Unit) {

    composable(ROUTE_RECITERS) {
        val viewModel: RecitersViewModel = getViewModel()
        RecitersScreen(uiState = viewModel.uiState, navigate)
    }

    composable(
        ROUTE_RECITER_RECITATIONS,
        listOf(navArgument(PARAM_RECITER_ID) { type = NavType.StringType }),
    ) {
        val viewModel = getViewModel<RecitationsViewModel>{ parametersOf(recitationsArgs(it.savedStateHandle))}

        RecitationsScreen(
            uiState = viewModel.uiState,
            popBackStack = popBackStack,
            play = viewModel::play,
            playFromSurah = viewModel::playFromSurah,
            download = viewModel::download
        )
    }
}

