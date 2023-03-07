package org.alquran.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val ColorScheme.DividerColor
  @Composable get() = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)

val MaterialTheme.DividerThickness get() = Dp.Hairline

@Composable
fun LineSeparator(
  modifier: Modifier = Modifier,
  color: Color = MaterialTheme.colorScheme.surfaceVariant,
  thickness: Dp = MaterialTheme.DividerThickness,
  startIndent: Dp = 0.dp,
) {
  Divider(
    modifier = modifier
      .padding(start = startIndent)
      .fillMaxWidth(),
    color = color,
    thickness = thickness,
  )
}