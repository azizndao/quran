package org.alquran.utils.extensions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PaddingValues.add(
  top: Dp = 0.dp,
  start: Dp = 0.dp,
  end: Dp = 0.dp,
  bottom: Dp = 0.dp
): PaddingValues {
  val layoutDirection = LocalLayoutDirection.current
  return PaddingValues(
    start = start + calculateStartPadding(layoutDirection),
    top = top + calculateTopPadding(),
    bottom = bottom + calculateBottomPadding(),
    end = end + calculateEndPadding(layoutDirection)
  )
}

@Composable
fun PaddingValues.add(
  horizontal: Dp = 0.dp,
  vertical: Dp = 0.dp,
): PaddingValues {
  val layoutDirection = LocalLayoutDirection.current
  return PaddingValues(
    start = horizontal + calculateStartPadding(layoutDirection),
    top = vertical + calculateTopPadding(),
    bottom = vertical + calculateBottomPadding(),
    end = horizontal + calculateEndPadding(layoutDirection)
  )
}