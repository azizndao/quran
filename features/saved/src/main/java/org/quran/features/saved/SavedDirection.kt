package org.quran.features.saved

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import arg.quran.models.quran.VerseKey
import org.koin.androidx.compose.getViewModel

const val SavedRoute = "saved"

fun NavGraphBuilder.savedDestination(
  contentPadding: PaddingValues,
  navigateToMore: () -> Unit,
  navigateToSearch: () -> Unit,
  navigateToPage: (page: Int, key: VerseKey?) -> Unit,
) {
  composable(SavedRoute) {
    SavedScreen(
      viewModel = getViewModel(),
      contentPadding = contentPadding,
      navigateToMore = navigateToMore,
      navigateToSearch = navigateToSearch,
      navigateToPage = navigateToPage,
    )
  }
}