package org.quran.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

data class AppBarParams(val modifier: Modifier, val elevate: Boolean = false)

@Composable
fun CustomScrollView(
  isScrollable: Boolean,
  topAppBar: @Composable AppBarParams.() -> Unit,
  content: (PaddingValues) -> Unit
) {
  val windowInsets = WindowInsets.statusBars.asPaddingValues()
  val toolbarHeight by remember { derivedStateOf { 56.dp + windowInsets.calculateTopPadding() } }

  val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
  var toolbarOffsetHeightPx by remember { mutableStateOf(0f) }

  val nestedScrollConnection = remember {
    object : NestedScrollConnection {
      override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta = available.y
        val newOffset = toolbarOffsetHeightPx + delta
        toolbarOffsetHeightPx = newOffset.coerceIn(-toolbarHeightPx, 0f)
        return Offset.Zero
      }
    }
  }

  val modifier = if (isScrollable) {
    Modifier
      .fillMaxSize()
      .nestedScroll(nestedScrollConnection)
  } else {
    Modifier.fillMaxSize()
  }

  Box(modifier) {

    content(
      PaddingValues(
        top = toolbarHeight,
        bottom = 56.dp + windowInsets.calculateBottomPadding(),
      )
    )

    topAppBar(
      AppBarParams(
        Modifier
          .height(toolbarHeight)
          .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.roundToInt()) },
      )
    )
  }
}