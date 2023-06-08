package org.quran.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.quran.ui.R


@Composable
fun SwitchListItem(
  modifier: Modifier = Modifier,
  checked: Boolean,
  enabled: Boolean = true,
  onCheckedChange: (Boolean) -> Unit,
  leadingContent: @Composable (() -> Unit)? = null,
  supportingContent: @Composable (() -> Unit)? = null,
  overlineContent: @Composable (() -> Unit)? = null,
  colors: ListItemColors = ListItemDefaults.colors(),
  tonalElevation: Dp = ListItemDefaults.Elevation,
  shadowElevation: Dp = ListItemDefaults.Elevation,
  headlineContent: @Composable () -> Unit,
) {
  ListItem(
    modifier = modifier,
    colors = colors,
    tonalElevation = tonalElevation,
    shadowElevation = shadowElevation,
    leadingContent = leadingContent,
    headlineContent = headlineContent,
    supportingContent = supportingContent,
    overlineContent = overlineContent,
    trailingContent = {
      Row(modifier = Modifier.width(72.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(
          Modifier
            .width(3.dp)
            .height(38.dp)
            .background(
              MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
              CircleShape
            )
        )
        Switch(
          checked = checked,
          onCheckedChange = onCheckedChange,
          enabled = enabled,
          modifier = Modifier
            .weight(1f)
            .padding(start = 16.dp)
        )
      }
    }
  )
}

@Composable
fun RadioButtonListItem(
  modifier: Modifier = Modifier,
  selected: Boolean,
  enabled: Boolean = true,
  onClick: () -> Unit,
  leadingContent: @Composable (() -> Unit)? = null,
  secondaryText: @Composable (() -> Unit)? = null,
  overlineContent: @Composable (() -> Unit)? = null,
  colors: ListItemColors = ListItemDefaults.colors(),
  tonalElevation: Dp = ListItemDefaults.Elevation,
  shadowElevation: Dp = ListItemDefaults.Elevation,
  headlineContent: @Composable () -> Unit,
) {
  ListItem(
    modifier = modifier.clickable(enabled) { onClick() },
    leadingContent = leadingContent,
    headlineContent = headlineContent,
    supportingContent = secondaryText,
    overlineContent = overlineContent,
    colors = colors,
    tonalElevation = tonalElevation,
    shadowElevation = shadowElevation,
    trailingContent = {
      RadioButton(
        selected = selected,
        onClick = onClick,
        enabled = enabled,
      )
    }
  )
}

@Composable
fun ExpendableListItem(
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  leadingContent: @Composable (() -> Unit)? = null,
  secondaryText: @Composable (() -> Unit)? = null,
  overlineContent: @Composable (() -> Unit)? = null,
  headlineContent: @Composable () -> Unit,
  colors: ListItemColors = ListItemDefaults.colors(),
  tonalElevation: Dp = ListItemDefaults.Elevation,
  shadowElevation: Dp = ListItemDefaults.Elevation,
  content: @Composable (ColumnScope.() -> Unit),
) {

  var expanded by remember { mutableStateOf(false) }
  val rotation by animateFloatAsState(if (expanded) 180f else 0f)

  Column(modifier = modifier.clickable(!expanded) { expanded = !expanded }) {
    ListItem(
      leadingContent = leadingContent,
      headlineContent = headlineContent,
      supportingContent = secondaryText,
      overlineContent = overlineContent,
      colors = colors,
      tonalElevation = tonalElevation,
      shadowElevation = shadowElevation,
      trailingContent = {
        IconButton(onClick = { expanded = !expanded }, enabled = enabled) {
          Icon(
            painter = painterResource(R.drawable.ic_arrow_down),
            contentDescription = null,
            modifier = Modifier.rotate(rotation)
          )
        }
      })

    AnimatedVisibility(expanded) {
      Column {
        content()
        LineSeparator()
      }
    }
  }
}
