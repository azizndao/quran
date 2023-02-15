package org.alquran.ui.screen.pager

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Transition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import org.alquran.R
import org.alquran.ui.components.AnimatedCounter
import org.alquran.ui.components.BackButton
import org.alquran.ui.components.MuslimsTopAppBarDefaults
import org.alquran.ui.components.SearchButton
import org.alquran.ui.screen.pager.components.AyaMenuSheet
import org.alquran.ui.screen.pager.components.MushafPageView
import org.alquran.ui.screen.pager.components.ProviderQuranTextStyle
import org.alquran.ui.screen.pager.components.TranslationPageItem
import org.alquran.ui.uistate.MushafPage
import org.alquran.ui.uistate.PagerUiState
import org.alquran.ui.uistate.QuranPageItem
import org.alquran.ui.uistate.TranslationPage
import org.alquran.views.DisplayModeButton
import org.quram.common.model.VerseKey
import org.quran.datastore.DisplayMode
import kotlin.math.abs


@Composable
internal fun QuranPagerScreen(
    uiState: PagerUiState,
    popBackStack: () -> Unit,
    onDisplayChange: (DisplayMode) -> Unit,
    onAyahEvent: (AyahEvent) -> Unit,
    pageProvider: @Composable (DisplayMode, page: Int, version: Int) -> QuranPageItem?,
    navigate: (String) -> Unit,
    onFullscreen: (Boolean) -> Unit,
    fullscreenTransition: Transition<Boolean>,
    pagerState: PagerState = rememberPagerState(uiState.initialPage - 1)
) {

    var isFullscreen by remember { mutableStateOf(false) }

    LaunchedEffect(isFullscreen) {
        onFullscreen(isFullscreen)
    }

    var shownAyaMenu: VerseKey? by remember { mutableStateOf(null) }

    if (shownAyaMenu != null) {
        AyaMenuSheet{shownAyaMenu = null}
    }

    Scaffold(
        topBar = {
            val currentPage by remember { derivedStateOf { pagerState.currentPage + 1 } }
            fullscreenTransition.AnimatedVisibility(
                visible = { !it },
                enter = slideInVertically { -it },
                exit = slideOutVertically { -it },
            ) {
                TopBar(
                    page = currentPage,
                    displayMode = uiState.displayMode,
                    navigate = navigate,
                    onPopBackStack = popBackStack,
                    onTextEvent = onDisplayChange
                )
            }
        },
    ) { innerPadding ->

        LaunchedEffect(uiState) {
            val currentIndex = pagerState.currentPage
            if (uiState.playingPage != null && uiState.playingPage != currentIndex) {
                val page = uiState.playingPage - 1
                if (abs(page - currentIndex) == 1) {
                    pagerState.animateScrollToPage(page)
                }
            }
        }

        HandleFullscreen(isFullscreen) { isFullscreen = !isFullscreen }

        val safeContentInsets = WindowInsets.displayCutout.asPaddingValues()

        val contentPadding = PaddingValues(
            top = safeContentInsets.calculateTopPadding(),
            bottom = 16.dp + safeContentInsets.calculateBottomPadding(),
            start = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
            end = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
        )

        val currentOnAyahEvent by rememberUpdatedState { event: AyahEvent ->
            if (event is AyahEvent.AyahLongPressed) {
                // navigate(directionToAyahMenu(event.sura, event.ayah))
                shownAyaMenu = event.verseKey
            } else if (event is AyahEvent.AyahPressed) {
                isFullscreen = !isFullscreen
            }
            onAyahEvent(event)
        }

        HorizontalPager(
            pageCount = 604,
            state = pagerState,
            reverseLayout = true,
            beyondBoundsPageCount = 2,
            pageSpacing = 1.dp,
            key = { it },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable(remember { MutableInteractionSource() }, null) {
                    isFullscreen = !isFullscreen
                }
        ) { pageIndex ->
            val page = pageProvider(uiState.displayMode, pageIndex + 1, uiState.version)

            ProviderQuranTextStyle(page = pageIndex + 1, fontScale = uiState.quranFontScale) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    when (page) {
                        is MushafPage -> MushafPageView(
                            page = page,
                            onAyahEvent = currentOnAyahEvent,
                            modifier = Modifier.padding(contentPadding)
                        )

                        is TranslationPage -> TranslationPageItem(
                            page = page,
                            contentPadding = contentPadding,
                            onAyahEvent = currentOnAyahEvent
                        )

                        null -> CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HandleFullscreen(
    isFullscreen: Boolean,
    onFullscreenChange: (Boolean) -> Unit,
) {

    BackHandler(isFullscreen) { onFullscreenChange(false) }

    val view = LocalView.current
    val insetsController = remember(view) {
        val activity = view.context as Activity
        WindowCompat.getInsetsController(activity.window, view)
    }

    LaunchedEffect(isFullscreen) {
        insetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE
        if (isFullscreen) {
            insetsController.hide(WindowInsetsCompat.Type.systemBars())
        } else {
            insetsController.show(WindowInsetsCompat.Type.systemBars())
        }
    }
}

@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    page: Int,
    displayMode: DisplayMode,
    navigate: (String) -> Unit,
    onPopBackStack: () -> Unit,
    onTextEvent: (DisplayMode) -> Unit,
) {
    TopAppBar(
        title = {
            AnimatedCounter(count = page) {
                Text(stringResource(R.string.page_x, it))
            }
        },
        modifier = modifier,
        navigationIcon = { BackButton(onClick = onPopBackStack) },
        actions = {
            SearchButton(onClick = { })

            DisplayModeButton(displayMode = displayMode) { onTextEvent(it) }

            IconButton(onClick = { }) {
                Icon(painterResource(id = R.drawable.ic_more_vert), null)
            }
        },
        colors = MuslimsTopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.background)
    )
}
