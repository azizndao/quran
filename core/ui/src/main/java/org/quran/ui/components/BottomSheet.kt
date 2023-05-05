package org.alquran.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomSheetDragHandler(modifier: Modifier = Modifier) {
  Box(modifier = modifier.fillMaxWidth()) {
    Box(
      modifier = Modifier
        .align(Alignment.Center)
        .padding(top = 12.dp, bottom = 24.dp)
        .size(width = 32.dp, height = 4.dp)
        .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(0.4f), CircleShape),
    )
  }
}