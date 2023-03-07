package org.alquran.views

import androidx.compose.animation.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import org.alquran.R
import org.quran.datastore.DisplayMode


@Composable
fun DisplayModeButton(
  displayMode: DisplayMode?,
  enabled: Boolean = true,
  onClick: (DisplayMode) -> Unit,
) {
  IconButton(
    enabled = enabled,
    onClick = { onClick(if (displayMode == DisplayMode.QURAN) DisplayMode.QURAN_TRANSLATION else DisplayMode.QURAN) }
  ) {
    AnimatedContent(
      targetState = displayMode,
      transitionSpec = {
        fadeIn() + slideIn {
          IntOffset(0, it.height)
        } with fadeOut() + slideOut { IntOffset(0, -it.height) }
      }
    ) { target ->
      when (target) {
        DisplayMode.QURAN_TRANSLATION -> Icon(
          painterResource(R.drawable.ic_language),
          null
        )

        else -> Icon(painterResource(R.drawable.ic_menu_book), null)
      }
    }
  }
}
