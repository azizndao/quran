package org.alquran.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.alquran.utils.extensions.CustomDialogPosition
import org.alquran.utils.extensions.customDialogModifier
import kotlin.math.max


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BottomDialog(
  modifier: Modifier = Modifier,
  onDismissRequest: () -> Unit,
  confirmButton: @Composable() (() -> Unit)? = null,
  dismissButton: @Composable (() -> Unit)? = null,
  icon: @Composable (() -> Unit)? = null,
  title: @Composable (() -> Unit)? = null,
  textContentPadding: PaddingValues = DialogContentPadding,
  shape: Shape = MaterialTheme.shapes.extraLarge,
  containerColor: Color = MaterialTheme.colorScheme.surface,
  tonalElevation: Dp = 6.dp,
  iconContentColor: Color = MaterialTheme.colorScheme.secondary,
  titleContentColor: Color = MaterialTheme.colorScheme.onSurface,
  textContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
  properties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
  content: @Composable (() -> Unit)? = null,
) {

  Dialog(onDismissRequest = onDismissRequest, properties = properties) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = null,
          onClick = onDismissRequest,
        )
    )
    AlertDialogContent(
      modifier = modifier
        .animateContentSize()
        .fillMaxWidth()
        .customDialogModifier(CustomDialogPosition.BOTTOM),
      buttons = {
        if (dismissButton != null || confirmButton != null)
          AlertDialogFlowRow(
            mainAxisSpacing = ButtonsMainAxisSpacing,
            crossAxisSpacing = ButtonsCrossAxisSpacing
          ) {
            dismissButton?.invoke()
            confirmButton?.invoke()
          }
      },
      icon = icon,
      title = title,
      text = content,
      shape = shape,
      containerColor = containerColor,
      tonalElevation = tonalElevation,
      buttonContentColor = MaterialTheme.colorScheme.primary,
      iconContentColor = iconContentColor,
      titleContentColor = titleContentColor,
      textContentColor = textContentColor,
      textContentPadding = textContentPadding
    )
  }
}

@Composable
private fun AlertDialogContent(
  buttons: @Composable () -> Unit,
  modifier: Modifier = Modifier,
  icon: (@Composable () -> Unit)?,
  title: (@Composable () -> Unit)?,
  text: @Composable (() -> Unit)?,
  shape: Shape,
  containerColor: Color,
  tonalElevation: Dp,
  buttonContentColor: Color,
  iconContentColor: Color,
  textContentPadding: PaddingValues,
  titleContentColor: Color,
  textContentColor: Color,
) {
  Surface(
    modifier = modifier,
    shape = shape,
    color = containerColor,
    tonalElevation = tonalElevation,
  ) {
    Column(
      modifier = Modifier
        .sizeIn(minWidth = MinWidth, maxWidth = MaxWidth)
        .padding(DialogPadding),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      icon?.let {
        CompositionLocalProvider(LocalContentColor provides iconContentColor) {
          Box(
            Modifier
              .padding(IconPadding)
              .align(Alignment.CenterHorizontally)
          ) {
            icon()
          }
        }
      }
      title?.let {
        CompositionLocalProvider(LocalContentColor provides titleContentColor) {
          val textStyle = MaterialTheme.typography.headlineSmall
          ProvideTextStyle(textStyle) {
            Box(
              // Align the title to the center when an icon is present.
              Modifier
                .padding(TitlePadding)
                .align(
                  if (icon == null) {
                    Alignment.Start
                  } else {
                    Alignment.CenterHorizontally
                  }
                )
            ) {
              title()
            }
          }
        }
      }
      text?.let {
        CompositionLocalProvider(LocalContentColor provides textContentColor) {
          val textStyle =
            MaterialTheme.typography.bodyMedium
          ProvideTextStyle(textStyle) {
            Box(
              Modifier
                .weight(weight = 1f, fill = false)
                .padding(textContentPadding)
                .align(Alignment.Start)
            ) {
              text()
            }
          }
        }
      }
      Box(
        modifier = Modifier
          .align(Alignment.End)
          .padding(DialogContentPadding)
      ) {
        CompositionLocalProvider(LocalContentColor provides buttonContentColor) {
          ProvideTextStyle(value = MaterialTheme.typography.labelLarge, content = buttons)
        }
      }
    }
  }
}


/**
 * Simple clone of FlowRow that arranges its children in a horizontal flow with limited
 * customization.
 */
@Composable
private fun AlertDialogFlowRow(
  mainAxisSpacing: Dp,
  crossAxisSpacing: Dp,
  content: @Composable () -> Unit,
) {
  Layout(content) { measurables, constraints ->
    val sequences = mutableListOf<List<Placeable>>()
    val crossAxisSizes = mutableListOf<Int>()
    val crossAxisPositions = mutableListOf<Int>()

    var mainAxisSpace = 0
    var crossAxisSpace = 0

    val currentSequence = mutableListOf<Placeable>()
    var currentMainAxisSize = 0
    var currentCrossAxisSize = 0

    // Return whether the placeable can be added to the current sequence.
    fun canAddToCurrentSequence(placeable: Placeable) =
      currentSequence.isEmpty() || currentMainAxisSize + mainAxisSpacing.roundToPx() +
        placeable.width <= constraints.maxWidth

    // Store current sequence information and start a new sequence.
    fun startNewSequence() {
      if (sequences.isNotEmpty()) {
        crossAxisSpace += crossAxisSpacing.roundToPx()
      }
      sequences += currentSequence.toList()
      crossAxisSizes += currentCrossAxisSize
      crossAxisPositions += crossAxisSpace

      crossAxisSpace += currentCrossAxisSize
      mainAxisSpace = max(mainAxisSpace, currentMainAxisSize)

      currentSequence.clear()
      currentMainAxisSize = 0
      currentCrossAxisSize = 0
    }

    for (measurable in measurables) {
      // Ask the child for its preferred size.
      val placeable = measurable.measure(constraints)

      // Start a new sequence if there is not enough space.
      if (!canAddToCurrentSequence(placeable)) startNewSequence()

      // Add the child to the current sequence.
      if (currentSequence.isNotEmpty()) {
        currentMainAxisSize += mainAxisSpacing.roundToPx()
      }
      currentSequence.add(placeable)
      currentMainAxisSize += placeable.width
      currentCrossAxisSize = max(currentCrossAxisSize, placeable.height)
    }

    if (currentSequence.isNotEmpty()) startNewSequence()

    val mainAxisLayoutSize = max(mainAxisSpace, constraints.minWidth)

    val crossAxisLayoutSize = max(crossAxisSpace, constraints.minHeight)

    val layoutWidth = mainAxisLayoutSize

    val layoutHeight = crossAxisLayoutSize

    layout(layoutWidth, layoutHeight) {
      sequences.forEachIndexed { i, placeables ->
        val childrenMainAxisSizes = IntArray(placeables.size) { j ->
          placeables[j].width +
            if (j < placeables.lastIndex) mainAxisSpacing.roundToPx() else 0
        }
        val arrangement = Arrangement.Bottom
        // TODO(soboleva): rtl support
        // Handle vertical direction
        val mainAxisPositions = IntArray(childrenMainAxisSizes.size) { 0 }
        with(arrangement) {
          arrange(mainAxisLayoutSize, childrenMainAxisSizes, mainAxisPositions)
        }
        placeables.forEachIndexed { j, placeable ->
          placeable.place(
            x = mainAxisPositions[j],
            y = crossAxisPositions[i]
          )
        }
      }
    }
  }
}

// Paddings for each of the dialog's parts.
private val DialogPadding = PaddingValues(top = 24.dp, bottom = 18.dp)
private val IconPadding = PaddingValues(horizontal = 24.dp)
private val TitlePadding = PaddingValues(horizontal = 24.dp)
val DialogContentNoPadding = PaddingValues()
private val DialogContentPadding = PaddingValues(horizontal = 24.dp)

private val MinWidth = 280.dp
private val MaxWidth = 560.dp

private val ButtonsMainAxisSpacing = 8.dp
private val ButtonsCrossAxisSpacing = 12.dp
