package org.quran.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AnimatedCounter(
  modifier: Modifier = Modifier,
  count: Int,
  content: @Composable (targetState: Int) -> Unit,
) {
  val transition = updateTransition(targetState = count, label = "Count slider")
  transition.AnimatedContent(
    modifier = modifier,
    transitionSpec = {
      if (targetState > initialState) {
        (slideInVertically { height -> height } + fadeIn()) togetherWith slideOutVertically { height -> -height } + fadeOut()
      } else {
        (slideInVertically { height -> -height } + fadeIn()) togetherWith slideOutVertically { height -> height } + fadeOut()
      }
    },
    content = { content(it) },
    contentAlignment = Alignment.Center,
  )
}
