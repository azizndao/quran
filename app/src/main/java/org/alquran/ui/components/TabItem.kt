
package org.alquran.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp


@Composable
fun TabBar(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(containerColor),
    divider: @Composable () -> Unit = { /*Divider()*/ },
    indicator: @Composable (tabPositions: List<TabPosition>) -> Unit = @Composable { tabPositions ->
        TabRowDefaults.Indicator(
            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
            height = 2.dp
        )
    },
    tabs: @Composable () -> Unit,
) {
    TabRow(
        modifier = modifier
            .fillMaxWidth()
            .background(containerColor),
        selectedTabIndex = selectedTabIndex,
        containerColor = containerColor,
        contentColor = contentColor,
        indicator = indicator,
        divider = divider,
        tabs = tabs,
    )
}

@Composable
fun ScrollableTabBar(
    modifier: Modifier = Modifier,
    lazyPagerState: PagerState,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(containerColor),
    divider: @Composable () -> Unit = { /*Divider()*/ },
    tabs: @Composable () -> Unit,
) {

    ScrollableTabRow(
        modifier = modifier
            .fillMaxWidth()
            .background(containerColor),
        selectedTabIndex = lazyPagerState.currentPage,
        containerColor = containerColor,
        contentColor = contentColor,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.pagerTabIndicatorOffset(lazyPagerState, tabPositions),
                height = 2.dp
            )
        },
        divider = divider,
        edgePadding = 0.dp,
        tabs = tabs,
    )
}

@Composable
fun TabItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    selectedContentColor: Color = MaterialTheme.colorScheme.primary,
    unselectedContentColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
    content: @Composable ColumnScope.() -> Unit,
) {
    Tab(
        selected = selected,
        onClick = onClick,
        selectedContentColor = selectedContentColor,
        unselectedContentColor = unselectedContentColor,
        modifier = modifier.height(48.dp),
        content = {
            ProvideTextStyle(MaterialTheme.typography.labelLarge) {
                Column(modifier = Modifier.padding(horizontal = 12.dp), content = content)
            }
        },
        interactionSource = interactionSource,
        enabled = enabled,
    )
}

/**
 * This indicator syncs up a [TabRow] or [ScrollableTabRow] tab indicator with a
 * [HorizontalPager] or [VerticalPager]. See the sample for a full demonstration.
 *
 */
fun Modifier.pagerTabIndicatorOffset(
    lazyPagerState: PagerState,
    tabPositions: List<TabPosition>,
    pageIndexMapping: (Int) -> Int = { it },
): Modifier = layout { measurable, constraints ->
    if (tabPositions.isEmpty()) {
        // If there are no pages, nothing to show
        layout(constraints.maxWidth, 0) {}
    } else {
        val currentPage =
            minOf(tabPositions.lastIndex, pageIndexMapping(lazyPagerState.currentPage))
        val currentTab = tabPositions[currentPage]
        val previousTab = tabPositions.getOrNull(currentPage - 1)
        val nextTab = tabPositions.getOrNull(currentPage + 1)
        val fraction = lazyPagerState.currentPageOffsetFraction
        val indicatorWidth = if (fraction > 0 && nextTab != null) {
            lerp(currentTab.width, nextTab.width, fraction).roundToPx()
        } else if (fraction < 0 && previousTab != null) {
            lerp(currentTab.width, previousTab.width, -fraction).roundToPx()
        } else {
            currentTab.width.roundToPx()
        }
        val indicatorOffset = if (fraction > 0 && nextTab != null) {
            lerp(currentTab.left, nextTab.left, fraction).roundToPx()
        } else if (fraction < 0 && previousTab != null) {
            lerp(currentTab.left, previousTab.left, -fraction).roundToPx()
        } else {
            currentTab.left.roundToPx()
        }
        val placeable = measurable.measure(
            Constraints(
                minWidth = indicatorWidth,
                maxWidth = indicatorWidth,
                minHeight = 0,
                maxHeight = constraints.maxHeight
            )
        )
        layout(constraints.maxWidth, maxOf(placeable.height, constraints.minHeight)) {
            placeable.placeRelative(
                indicatorOffset,
                maxOf(constraints.minHeight - placeable.height, 0)
            )
        }
    }
}