package org.quran.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun <T> SmoothCrossFade(
  targetState: T,
  modifier: Modifier = Modifier,
  animationSpec: FiniteAnimationSpec<Float> = tween(),
  content: @Composable (T) -> Unit,
) {
  Crossfade(
    targetState = targetState,
    modifier = modifier,
    animationSpec = animationSpec,
    content = content, label = "SmoothCrossFade"
  )
}


@Composable
private fun BoundsButton(
  modifier: Modifier = Modifier,
  enabled: Boolean,
  @DrawableRes enabledIconId: Int,
  @DrawableRes disabledIconId: Int,
  onClick: () -> Unit,
) {

  val enabledColor = MaterialTheme.colorScheme.primary
  val disabledColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)

  val transitionState = remember(enabled) { MutableTransitionState(enabled) }
  val transition = updateTransition(targetState = transitionState, label = "icon_transition")

  val iconSize by transition.animateFloat(label = "icon size animation",
    targetValueByState = { if (it.currentState) 1f else 1f },
    transitionSpec = {
      keyframes {
        0.75f at 60
        2f at 150
        0.8f at 250
        durationMillis = 300
      }
    })

  val iconTint by transition.animateColor(label = "icon color animation",
    transitionSpec = { keyframes { durationMillis = 300; delayMillis = 100 } },
    targetValueByState = { if (it.currentState) enabledColor else disabledColor })

  val drawableId by transition.animateInt(
    label = "Drawable id",
  ) {
    if (it.currentState) enabledIconId else disabledIconId
  }

  IconButton(modifier = modifier.scale(iconSize), onClick = onClick) {
    Icon(
      painter = painterResource(id = drawableId),
      contentDescription = null,
      tint = iconTint,
    )
  }
}
