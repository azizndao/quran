package org.alquran.ui.components

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedCounter(
  modifier: Modifier = Modifier,
  count: Int,
  content: @Composable AnimatedVisibilityScope.(targetState: Int) -> Unit,
) {
  AnimatedContent(
    modifier = modifier,
    targetState = count,
    transitionSpec = {
      if (targetState > initialState) {
        slideInVertically { height -> height } + fadeIn() with
          slideOutVertically { height -> -height } + fadeOut()
      } else {
        slideInVertically { height -> -height } + fadeIn() with
          slideOutVertically { height -> height } + fadeOut()
      }
    },
    content = content,
    contentAlignment = Alignment.Center
  )
}
