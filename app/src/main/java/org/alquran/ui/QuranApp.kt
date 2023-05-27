package org.alquran.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.alquran.ui.components.NiaNavigationBar
import org.alquran.ui.components.NiaNavigationBarItem
import org.quran.core.audio.PlaybackConnection
import org.quran.features.home.HomeQuranRoute
import org.quran.features.saved.SavedRoute
import org.quran.ui.R


@Composable
fun QuranApp(
  playbackConnection: PlaybackConnection,
  navController: NavHostController = rememberNavController()
) {
  Scaffold(
    bottomBar = { BottomBar(navController = navController) },
    contentWindowInsets = WindowInsets.navigationBars,
  ) { innerPadding ->
    NabGraph(contentPadding = innerPadding, playbackConnection, navController)
  }
}

@Composable
private fun BottomBar(navController: NavHostController) {
  val entry by navController.currentBackStackEntryAsState()

  val visible by remember {
    derivedStateOf {
      val route = entry?.destination?.route
      route == HomeQuranRoute || route == SavedRoute
    }
  }

  AnimatedVisibility(
    visible = visible,
    enter = fadeIn() + slideInVertically { it },
    exit = fadeOut() + slideOutVertically { it }
  ) {
    BottomBar(entry?.destination?.route) {
      navController.navigate(it) {
        restoreState = true
        popUpTo(HomeQuranRoute) { saveState = true }
      }
    }
  }
}

@Composable
fun BottomBar(currentRoute: String?, navigate: (route: String) -> Unit) {
  NiaNavigationBar {
    NiaNavigationBarItem(
      selected = currentRoute == HomeQuranRoute,
      onClick = { navigate(HomeQuranRoute) },
      icon = { Icon(painterResource(id = R.drawable.home), null) },
      selectedIcon = { Icon(painterResource(id = R.drawable.home_filled), null) },
      label = { Text(text = stringResource(id = R.string.home)) }
    )
    NiaNavigationBarItem(
      selected = currentRoute == SavedRoute,
      onClick = { navigate(SavedRoute) },
      icon = { Icon(painterResource(id = R.drawable.bookmarks), null) },
      selectedIcon = { Icon(painterResource(id = R.drawable.bookmarks_filled), null) },
      label = { Text(text = stringResource(id = R.string.saved)) }
    )
  }
}
