package org.muslimsapp.quran.search

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import arg.quran.models.quran.VerseKey
import org.koin.androidx.compose.getViewModel

private const val SearchRoute = "quran/search"

fun NavController.navigateToSearch() = navigate(SearchRoute)

fun NavGraphBuilder.searchDestination(
  popBackStack: () -> Unit,
  navigateToPage: (page: Int, suraAyah: VerseKey) -> Unit,
) {
  composable(SearchRoute) {
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