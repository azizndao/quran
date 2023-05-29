package org.quran.features.pager

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import arg.quran.models.quran.VerseKey
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import org.quran.datastore.DisplayMode
import org.quran.features.pager.components.AudioBottomBar
import org.quran.features.pager.components.DisplayModeButton
import org.quran.features.pager.components.ProviderQuranTextStyle
import org.quran.features.pager.components.menu.AddNoteBottomSheet
import org.quran.features.pager.components.menu.AudioMenuSheet
import org.quran.features.pager.components.menu.SelectBookmarkTagSheet
import org.quran.features.pager.components.menu.ShowBookmarksSheet
import org.quran.features.pager.components.menu.VerseMenuSheet
import org.quran.features.pager.components.menu.VerseTafsirSheet
import org.quran.features.pager.components.pages.QuranPageView
import org.quran.features.pager.components.pages.TranslationPageItem
import org.quran.features.pager.uiState.DialogUiState
import org.quran.features.pager.uiState.PageItem
import org.quran.features.pager.uiState.QuranEvent
import org.quran.features.pager.uiState.QuranPage
import org.quran.features.pager.uiState.QuranPagerUiState
import org.quran.features.pager.uiState.TranslationPage
import org.quran.ui.R
import org.quran.ui.components.AnimatedCounter
import org.quran.ui.components.BackButton
import org.quran.ui.components.MuslimsTopAppBarDefaults
import org.quran.ui.components.SearchButton
import kotlin.math.abs


@Composable
internal fun QuranPagerScreen(
  viewModel: QuranPagerViewModel,
  popBackStack: () -> Unit,
  pagerState: PagerState = rememberPagerState(viewModel.initialPage - 1) { 604 },
  navigateToSearch: () -> Unit,
  navigateToTranslations: () -> Unit,
  navigateToShare: (verse: VerseKey) -> Unit,
) {

  Box {
    val currentPage by remember { derivedStateOf { pagerState.currentPage + 1 } }

    HandleFullscreen(viewModel.isFullscreen) { viewModel.toggleFullscreen() }

    LaunchedEffect(Unit) {
      viewModel.playingPageFlow
        .filter { it != currentPage }
        .collectLatest { page ->
          if (abs(page - currentPage) == 1) {
            pagerState.animateScrollToPage(page - 1)
          }
        }
    }

    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    QuranPaged(
      pagerState = pagerState,
      uiState = uiState,
      onEvent = viewModel::onEvent,
    ) { mode, page, version ->
      viewModel.pageFactory(mode, page, version)
    }

    DialogHandler(viewModel, navigateToShare = navigateToShare)

    AnimatedVisibility(
      visible = !viewModel.isFullscreen,
      enter = slideInVertically { -it },
      exit = slideOutVertically { -it },
      modifier = Modifier.align(Alignment.TopCenter)
    ) {
      TopBar(
        page = currentPage,
        displayMode = uiState.displayMode,
        navigateToSearch = navigateToSearch,
        navigateToTranslations = navigateToTranslations,
        onPopBackStack = popBackStack,
        onDisplayModeChange = viewModel::changeDisplayMode,
      )
    }

    val nowPlaying by viewModel.nowPlayingFlow.collectAsStateWithLifecycle()
    val visible by remember(viewModel, nowPlaying) {
      derivedStateOf {
        !viewModel.isFullscreen && nowPlaying != null
      }
    }

    AnimatedVisibility(
      visible = visible,
      enter = slideInVertically { it },
      exit = slideOutVertically { it },
      modifier = Modifier.align(Alignment.BottomCenter)
    ) {
      AudioBottomBar(
        uiState = nowPlaying!!,
        onEvent = viewModel::onEvent,
        onExpand =  viewModel::showAudioMenu,
      )
    }
  }
}

@Composable
internal fun DialogHandler(viewModel: QuranPagerViewModel, navigateToShare: (VerseKey) -> Unit) {
  when (val uiState = viewModel.dialogUiState) {
    is DialogUiState.CreateNote -> AddNoteBottomSheet(
      verse = uiState.verse,
      onDismissRequest = viewModel::onDismissRequest,
      onCreate = viewModel::createNote
    )

    is DialogUiState.SelectBookmarkTab -> SelectBookmarkTagSheet(
      uiState = uiState,
      onDismissRequest = viewModel::onDismissRequest,
      onCreate = viewModel::onCreateBookmarkAndTag,
      onSelect = viewModel::onCreateBookmark
    )

    is DialogUiState.ShowBookmarks -> ShowBookmarksSheet(
      uiState = uiState,
      onDelete = viewModel::removeBookmark,
      onDismissRequest = viewModel::onDismissRequest
    )

    is DialogUiState.VerseMenu -> VerseMenuSheet(
      uiState = uiState,
      onEvent = viewModel::onEvent,
      onDismissRequest = viewModel::onDismissRequest,
      navigateToShare = {
        viewModel.onDismissRequest()
        navigateToShare(uiState.verse)
      },
    )

    is DialogUiState.VerseTafsir -> VerseTafsirSheet(
      uiState = uiState,
      onDismissRequest = viewModel::onDismissRequest
    )

    is DialogUiState.AudioMenu -> AudioMenuSheet(
      uiState = uiState,
      onEvent = viewModel::onEvent,
      onDismissRequest = viewModel::onDismissRequest,
      showReciter = {},
      onPositionChange = {},
    )

    DialogUiState.None -> {}
  }
}

@Composable
private fun QuranPaged(
  modifier: Modifier = Modifier,
  pagerState: PagerState,
  uiState: QuranPagerUiState,
  onEvent: (QuranEvent) -> Unit,
  pageProvider: @Composable (DisplayMode, page: Int, version: Int) -> PageItem?
) {

  val currentOnQuranEvent by rememberUpdatedState(onEvent)

  HorizontalPager(
    state = pagerState,
    reverseLayout = true,
    // beyondBoundsPageCount = 1,
    pageSpacing = 1.dp,
    key = { it },
    modifier = modifier
      .background(MaterialTheme.colorScheme.surfaceVariant)
  ) { pageIndex ->
    Surface(modifier = Modifier.fillMaxSize()) {
      ProviderQuranTextStyle(page = pageIndex + 1, fontScale = uiState.quranFontScale) {

        when (val page = pageProvider(uiState.displayMode, pageIndex + 1, uiState.version)) {
          is QuranPage -> QuranPageView(page = page, onAyahEvent = currentOnQuranEvent)

          is TranslationPage -> TranslationPageItem(page = page, onEvent = currentOnQuranEvent)

          else -> {}
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
      WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
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
  onPopBackStack: () -> Unit,
  onDisplayModeChange: (DisplayMode) -> Unit,
  navigateToSearch: () -> Unit,
  navigateToTranslations: () -> Unit,
) {
  TopAppBar(
    title = {
      AnimatedCounter(count = page) {
        Text(stringResource(R.string.page_x, page))
      }
    },
    modifier = modifier,
    navigationIcon = { BackButton(onClick = onPopBackStack) },
    actions = {
      SearchButton(onClick = navigateToSearch)

      DisplayModeButton(displayMode = displayMode) { onDisplayModeChange(it) }

      IconButton(onClick = navigateToTranslations) {
        Icon(painterResource(id = R.drawable.ic_more_vert), null)
      }
    },
    colors = MuslimsTopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.background)
  )
}
