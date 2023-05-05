package org.quran.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.quran.ui.R

object MuslimsTopAppBarDefaults {
  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  fun largeTopAppBarColors(
    containerColor: Color = MaterialTheme.colorScheme.background,
  ): TopAppBarColors {
    return TopAppBarDefaults.largeTopAppBarColors(containerColor = containerColor)
  }

  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  fun mediumTopAppBarColors(
    containerColor: Color = MaterialTheme.colorScheme.background,
  ): TopAppBarColors {
    return TopAppBarDefaults.mediumTopAppBarColors(containerColor = containerColor)
  }

  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  fun smallTopAppBarColors(
    containerColor: Color = MaterialTheme.colorScheme.background
  ): TopAppBarColors {
    return TopAppBarDefaults.topAppBarColors(containerColor = containerColor)
  }
}

@Composable
fun BackButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
  IconButton(onClick = onClick, modifier = modifier) {
    Icon(
      painterResource(id = R.drawable.ic_arrow_back),
      contentDescription = stringResource(R.string.navigation_back)
    )
  }
}

@Composable
fun SearchButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
  IconButton(onClick = onClick, modifier = modifier) {
    Icon(
      painterResource(id = R.drawable.ic_search),
      contentDescription = stringResource(R.string.search)
    )
  }
}
