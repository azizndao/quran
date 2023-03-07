package org.alquran.ui.components

import androidx.annotation.StringRes
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.alquran.R


@Composable
fun NiaTopAppBar(
  @StringRes titleRes: Int,
  navigationIcon: Painter,
  navigationIconContentDescription: String?,
  actionIcon: Painter,
  actionIconContentDescription: String?,
  modifier: Modifier = Modifier,
  colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
  onNavigationClick: () -> Unit = {},
  onActionClick: () -> Unit = {}
) {
  CenterAlignedTopAppBar(
    title = { Text(text = stringResource(id = titleRes)) },
    navigationIcon = {
      IconButton(onClick = onNavigationClick) {
        Icon(
          painter = navigationIcon,
          contentDescription = navigationIconContentDescription,
          tint = MaterialTheme.colorScheme.onSurface
        )
      }
    },
    actions = {
      IconButton(onClick = onActionClick) {
        Icon(
          painter = actionIcon,
          contentDescription = actionIconContentDescription,
          tint = MaterialTheme.colorScheme.onSurface
        )
      }
    },
    colors = colors,
    modifier = modifier
  )
}

@Preview("Top App Bar")
@Composable
fun NiaTopAppBarPreview() {
  NiaTopAppBar(
    titleRes = R.string.app_name,
    navigationIcon = painterResource(id = R.drawable.ic_arrow_back),
    navigationIconContentDescription = "Navigation icon",
    actionIcon = painterResource(id = R.drawable.ic_arrow_back),
    actionIconContentDescription = "Action icon"
  )
}
