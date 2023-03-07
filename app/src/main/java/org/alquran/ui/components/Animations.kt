package org.alquran.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
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
    content = content
  )
}


@OptIn(ExperimentalAnimationApi::class)
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

  transition.AnimatedContent(contentAlignment = Alignment.Center) {
    IconButton(modifier = modifier.scale(iconSize), onClick = onClick) {
      Icon(
        painter = painterResource(id = drawableId),
        contentDescription = null,
        tint = iconTint,
      )
    }
  }
}


@Composable
fun CircularProgressbar1(
  size: Dp = 260.dp,
  foregroundIndicatorColor: Color = MaterialTheme.colorScheme.primary,
  shadowColor: Color = MaterialTheme.colorScheme.primaryContainer,
  indicatorThickness: Dp = 24.dp,
  dataUsage: Float = 60f,
  animationDuration: Int = 1000,
) {

  // It remembers the data usage value
  var dataUsageRemember by remember {
    mutableStateOf(-1f)
  }

  // This is to animate the foreground indicator
  val dataUsageAnimate = animateFloatAsState(
    targetValue = dataUsageRemember, animationSpec = tween(
      durationMillis = animationDuration
    )
  )

  // This is to start the animation when the activity is opened
  LaunchedEffect(Unit) {
    dataUsageRemember = dataUsage
  }

  Box(
    modifier = Modifier.size(size), contentAlignment = Alignment.Center
  ) {

    Canvas(
      modifier = Modifier.size(size)
    ) {

      // For shadow
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(shadowColor, Color.White),
          center = Offset(x = this.size.width / 2, y = this.size.height / 2),
          radius = this.size.height / 2
        ),
        radius = this.size.height / 2,
        center = Offset(x = this.size.width / 2, y = this.size.height / 2)
      )

      // This is the white circle that appears on the top of the shadow circle
      drawCircle(
        color = Color.White,
        radius = (size / 2 - indicatorThickness).toPx(),
        center = Offset(x = this.size.width / 2, y = this.size.height / 2)
      )

      // Convert the dataUsage to angle
      val sweepAngle = (dataUsageAnimate.value) * 360 / 100

      // Foreground indicator
      drawArc(
        color = foregroundIndicatorColor,
        startAngle = -90f,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(width = indicatorThickness.toPx(), cap = StrokeCap.Round),
        size = Size(
          width = (size - indicatorThickness).toPx(),
          height = (size - indicatorThickness).toPx()
        ),
        topLeft = Offset(
          x = (indicatorThickness / 2).toPx(), y = (indicatorThickness / 2).toPx()
        )
      )
    }
  }
}