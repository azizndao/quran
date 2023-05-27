package org.quran.features.pager.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.animation.togetherWith
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
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
        } togetherWith fadeOut() + slideOut { IntOffset(0, -it.height) }
      }, label = ""
    ) { target ->
      when (target) {
        DisplayMode.QURAN_TRANSLATION -> Icon(
          painterResource(org.quran.ui.R.drawable.ic_language),
          null
        )

        else -> Icon(painterResource(org.quran.ui.R.drawable.menu_book), null)
      }
    }
  }
}
