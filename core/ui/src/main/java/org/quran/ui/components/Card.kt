package org.alquran.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape

@Composable
fun SurfaceCard(
  modifier: Modifier = Modifier,
  shape: Shape = CardDefaults.shape,
  colors: CardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
  elevation: CardElevation = CardDefaults.cardElevation(),
  border: BorderStroke? = null,
  content: @Composable ColumnScope.() -> Unit
) {
  Card(
    modifier = modifier,
    shape = shape,
    colors = colors,
    elevation = elevation,
    border = border,
    content = content
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurfaceCard(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  shape: Shape = CardDefaults.shape,
  colors: CardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
  elevation: CardElevation = CardDefaults.cardElevation(),
  border: BorderStroke? = null,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
  content: @Composable ColumnScope.() -> Unit
) {
  Card(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    shape = shape,
    colors = colors,
    elevation = elevation,
    border = border,
    interactionSource = interactionSource,
    content = content
  )
}