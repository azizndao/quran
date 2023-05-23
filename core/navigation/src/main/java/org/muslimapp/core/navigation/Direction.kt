package org.muslimapp.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder

interface Direction {

  /**
   * Full route that will be added to the navigation graph
   */
  val route: String

  /**
   * [Composable] function that will be called to compose
   * the destination content in the screen, when the user
   * navigates to it.
   */
  fun content(builder: NavGraphBuilder)
}

