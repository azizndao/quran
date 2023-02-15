package org.alquran.ui.screen.audioSheet

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.alquran.audio.models.Reciter
import org.alquran.utils.lerp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlaybackBottomSheet(
    viewModel: PlaybackSheetViewModel,
    isFullscreen: Transition<Boolean>,
    sheetState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
    showReciter: (Reciter) -> Unit,
    onPositionChange: (Long) -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {

    val contentPadding = WindowInsets.systemBars.asPaddingValues()
    val coroutineScope = rememberCoroutineScope()
    val defaultPeekHeight =
        AudioBottomSheetCollapsedHeight + contentPadding.calculateBottomPadding()

    val sheetPeekHeight by isFullscreen.animateDp(label = "sheetPeekHeight") {
        if (it) 0.dp else defaultPeekHeight
    }

    BoxWithConstraints {
        val colorScheme = MaterialTheme.colorScheme
        BottomSheetScaffold(
            sheetBackgroundColor = colorScheme.background,
            sheetContentColor = colorScheme.onBackground,
            backgroundColor = colorScheme.background,
            contentColor = colorScheme.onBackground,
            content = content,
            scaffoldState = sheetState,
            sheetPeekHeight = sheetPeekHeight,
            sheetContent = {
                val uiState by viewModel.audioStateFlow.collectAsStateWithLifecycle()

                val fraction by remember {
                    derivedStateOf {
                        val progress = sheetState.bottomSheetState.progress
                        if (progress.to == BottomSheetValue.Expanded) {
                            progress.fraction
                        } else {
                            1 - progress.fraction
                        }
                    }
                }

                val collapseAlpha by remember {
                    derivedStateOf { lerp(1f, 0f, 0f, 0.3f, fraction) }
                }

                val expandedAlpha by remember {
                    derivedStateOf { lerp(0f, 1f, 0.2f, 0.5f, fraction) }
                }

                val density = LocalDensity.current

                val topPadding by remember {
                    derivedStateOf {
                        with(density) {
                            val maxTop = contentPadding.calculateTopPadding().toPx()
                            lerp(0f, maxTop, 0.8f, 1f, fraction)
                        }
                    }
                }

                val backgroundColor by remember {
                    derivedStateOf {
                        androidx.compose.ui.graphics.lerp(
                            colorScheme.background,
                            colorScheme.surface,
                            fraction
                        )
                    }
                }

                Surface(
                    color = Color.Transparent,
                    modifier = Modifier.drawBehind { drawRect(backgroundColor) },
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {

                    ExpandedContent(
                        onCollapse = { coroutineScope.launch { sheetState.bottomSheetState.collapse() } },
                        uiState = uiState,
                        onUiEvent = viewModel::onAudioEvent,
                        showReciter = showReciter,
                        onPositionChange = onPositionChange,
                        modifier = Modifier
                            .graphicsLayer {
                                alpha = expandedAlpha
                                translationY = topPadding
                            }
                            .zIndex(10f)
                    )

                    CollapsedBottomBar(
                        uiState = uiState,
                        onAudioEvent = viewModel::onAudioEvent,
                        modifier = Modifier
                            .zIndex(if (fraction < 1f) 11f else 0f)
                            .graphicsLayer { alpha = collapseAlpha },
                        onExpand = { coroutineScope.launch { sheetState.bottomSheetState.expand() } }
                    )
                }
            },
        )
    }
}

