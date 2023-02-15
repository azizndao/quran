package org.alquran.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
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
    val toolbarHeight by derivedStateOf { 56.dp + windowInsets.calculateTopPadding() }

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