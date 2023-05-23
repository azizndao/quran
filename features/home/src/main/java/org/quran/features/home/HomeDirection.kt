package org.quran.features.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import arg.quran.models.quran.VerseKey
import org.koin.androidx.compose.getViewModel

const val ROUTE_QURAN_HOME = "home"

 fun NavGraphBuilder.homeDestination(
  navigateToMore: () -> Unit,
  navigateToSearch: () -> Unit,
  navigateToPage: (page: Int, key: VerseKey?) -> Unit,
) {
  composable(ROUTE_QURAN_HOME) {
    HomeScreen(
      getViewModel(),
      navigateToPage =navigateToPage,
      navigateToMore = navigateToMore,
      navigateToSearch = navigateToSearch
    )
  }
}