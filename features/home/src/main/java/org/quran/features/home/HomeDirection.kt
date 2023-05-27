package org.quran.features.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import arg.quran.models.quran.VerseKey
import org.koin.androidx.compose.getViewModel

const val HomeQuranRoute = "home"

fun NavGraphBuilder.homeDestination(
  contentPadding: PaddingValues,
  navigateToMore: () -> Unit,
  navigateToSearch: () -> Unit,
  navigateToPage: (page: Int, key: VerseKey?) -> Unit,
) {
  composable(HomeQuranRoute) {
    HomeScreen(
      viewModel = getViewModel(),
      contentPadding = contentPadding,
      navigateToPage = navigateToPage,
      navigateToMore = navigateToMore,
      navigateToSearch = navigateToSearch
    )
  }
}