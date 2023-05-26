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
import kotlinx.coroutines.flow.map
import org.quran.datastore.DisplayMode
import org.quran.features.pager.components.AddNoteBottomSheet
import org.quran.features.pager.components.AudioBottomBar
import org.quran.features.pager.components.DisplayModeButton
import org.quran.features.pager.components.MushafPageView
import org.quran.features.pager.components.ProviderQuranTextStyle
import org.quran.features.pager.components.SelectBookmarkTagSheet
import org.quran.features.pager.components.ShowBookmarksSheet
import org.quran.features.pager.components.TranslationPageItem
import org.quran.features.pager.components.VerseMenuSheet
import org.quran.features.pager.components.VerseTafsirSheet
import org.quran.features.pager.uiState.DialogUiState
import org.quran.features.pager.uiState.MushafPage
import org.quran.features.pager.uiState.QuranEvent
import org.quran.features.pager.uiState.QuranPageItem
import org.quran.features.pager.uiState.QuranPagerUiState
import org.quran.features.pager.uiState.TranslationPage
import org.quran.ui.components.AnimatedCounter
import org.quran.ui.components.BackButton
import org.quran.ui.components.MuslimsTopAppBarDefaults
import org.quran.ui.components.SearchButton
import kotlin.math.abs
import org.quran.ui.R as Ui


@Composable
internal fun QuranPagerScreen(
  viewModel: QuranPagerViewModel,
  popBackStack: () -> Unit,
  pageProvider: @Composable (DisplayMode, page: Int, version: Int) -> QuranPageItem?,
  pagerState: PagerState = rememberPagerState(viewModel.args.page - 1) { 604 },
  navigateToSearch: () -> Unit,
  navigateToTranslations: () -> Unit,
  navigateToShare: (verse: VerseKey) -> Unit,
) {

  Box {
    val currentPage by remember { derivedStateOf { pagerState.currentPage + 1 } }

    HandleFullscreen(viewModel.isFullscreen) { viewModel.toggleFullscreen() }

    LaunchedEffect(Unit) {
      val currentIndex = pagerState.currentPage
      viewModel.playingPageFlow.map { it - 1 }
        .filter { it != pagerState.currentPage }
        .collectLatest { index ->
          if (abs(index - currentIndex) == 1) {
            pagerState.animateScrollToPage(index)
          }
        }
    }

    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()

    QuranPaged(
      pagerState = pagerState,
      uiState = uiState,
      onEvent = viewModel::onEvent,
      pageProvider = pageProvider,
    )

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
      AudioBottomBar(uiState = nowPlaying!!, onEvent = { viewModel.onEvent(it) }) {}
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

    DialogUiState.None -> {}
  }
}

@Composable
private fun QuranPaged(
  modifier: Modifier = Modifier,
  pagerState: PagerState,
  uiState: QuranPagerUiState,
  onEvent: (QuranEvent) -> Unit,
  pageProvider: @Composable (DisplayMode, page: Int, version: Int) -> QuranPageItem?
) {

  val currentOnQuranEvent by rememberUpdatedState { event: QuranEvent ->
    onEvent(event)
  }

  HorizontalPager(
    state = pagerState,
    reverseLayout = true,
    beyondBoundsPageCount = 1,
    pageSpacing = 1.dp,
    key = { it },
    modifier = modifier
      .background(MaterialTheme.colorScheme.surfaceVariant)
  ) { pageIndex ->

    ProviderQuranTextStyle(page = pageIndex + 1, fontScale = uiState.quranFontScale) {
      Surface(modifier = Modifier.fillMaxSize()) {

        when (val page = pageProvider(uiState.displayMode, pageIndex + 1, uiState.version)) {
          is MushafPage -> MushafPageView(
            page = page,
            onAyahEvent = currentOnQuranEvent,
          )

          is TranslationPage -> TranslationPageItem(
            page = page,
            onEvent = currentOnQuranEvent,
          )

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
  TopAppBar(title = {
    AnimatedCounter(count = page) {
      Text(stringResource(Ui.string.page_x, page))
    }
  }, modifier = modifier, navigationIcon = { BackButton(onClick = onPopBackStack) }, actions = {
    SearchButton(onClick = navigateToSearch)

    DisplayModeButton(displayMode = displayMode) { onDisplayModeChange(it) }

    IconButton(onClick = navigateToTranslations) {
      Icon(painterResource(id = Ui.drawable.ic_more_vert), null)
    }
  },
    colors = MuslimsTopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.background)
  )
}
