package org.alquran.ui.screen.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.koin.androidx.compose.getViewModel

const val ROUTE_QURAN_HOME = "home"

internal fun NavGraphBuilder.homeDestination(
  navigate: (String) -> Unit,
  navigateToMore: () -> Unit,
) {
  composable(ROUTE_QURAN_HOME) {
    HomeScreen(getViewModel(), navigate = navigate, navigateToMore = navigateToMore)
  }
}