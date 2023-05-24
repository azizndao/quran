package org.quran.features.saved

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import arg.quran.models.quran.VerseKey
import org.koin.androidx.compose.getViewModel

const val ROUTE_SAVED_HOME = "notes"

fun NavGraphBuilder.savedDestination(
  contentPadding: PaddingValues,
  navigateToMore: () -> Unit,
  navigateToSearch: () -> Unit,
  navigateToPage: (page: Int, key: VerseKey?) -> Unit,
) {
  composable(ROUTE_SAVED_HOME) {
    SavedScreen(
      viewModel = getViewModel(),
      contentPadding = contentPadding,
      navigateToPage = navigateToPage,
      navigateToMore = navigateToMore,
      navigateToSearch = navigateToSearch
    )
  }
}