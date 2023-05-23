package org.muslimsapp.quran.search

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import arg.quran.models.quran.VerseKey
import org.koin.androidx.compose.getViewModel

private const val QURAN_SEARCH_ROUTE = "quran/search"

fun directionToSearch() = QURAN_SEARCH_ROUTE

fun NavGraphBuilder.searchDestination(
  popBackStack: () -> Unit,
  navigateToPage: (page: Int, suraAyah: VerseKey) -> Unit,
) {
  composable(QURAN_SEARCH_ROUTE) {
    val viewModel = getViewModel<SearchViewModel>()

    LaunchedEffect(Unit) {
      viewModel.refresh()
    }

    SearchScreen(
      uiState = viewModel.uiState,
      popBackStack = popBackStack,
      navigateToQuranPage = navigateToPage,
    )
  }
}