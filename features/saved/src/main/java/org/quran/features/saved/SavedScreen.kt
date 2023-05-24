package org.quran.features.saved

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import arg.quran.models.quran.VerseKey
import kotlinx.coroutines.launch
import org.alquran.ui.components.TabBar
import org.alquran.ui.components.TabItem
import org.alquran.ui.components.pagerTabIndicatorOffset
import org.quran.features.saved.bookmarks.BookmarkTabView
import org.quran.features.saved.notes.NotesTabView
import org.quran.ui.R
import org.quran.ui.components.MuslimsTopAppBarDefaults

val tabItems = listOf(R.string.bookmarks, R.string.notes)

@Composable
fun SavedScreen(
  viewModel: SavedViewModel,
  contentPadding: PaddingValues,
  navigateToMore: () -> Unit,
  navigateToSearch: () -> Unit,
  navigateToPage: (page: Int, key: VerseKey?) -> Unit,
) {
  val pagerState = rememberPagerState { tabItems.size }

  val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
    state = rememberTopAppBarState(),
  )

  Column(modifier = Modifier.fillMaxSize()) {
    AppBar(
      scrollBehavior = scrollBehavior,
      pagerState = pagerState,
      search = navigateToSearch,
      navigateToMore = navigateToMore
    )

    HorizontalPager(state = pagerState) { position ->
      Box(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)) {
        when (position) {
          0 -> BookmarkTabView(contentPadding = contentPadding)
          1 -> NotesTabView(contentPadding = contentPadding)
          else -> throw IndexOutOfBoundsException()
        }
      }
    }
  }
}


@Composable
private fun AppBar(
  scrollBehavior: TopAppBarScrollBehavior,
  modifier: Modifier = Modifier,
  pagerState: PagerState,
  search: () -> Unit,
  navigateToMore: () -> Unit,
) {
  val scope = rememberCoroutineScope()

  val topBarColor = MuslimsTopAppBarDefaults.mediumTopAppBarColors()

  Column(modifier = modifier) {
    TopAppBar(
      scrollBehavior = scrollBehavior,
      title = { Text(stringResource(R.string.app_name)) },
      navigationIcon = {
        IconButton(onClick = { }) {
          Icon(
            painterResource(id = org.quran.ui.R.drawable.ic_notifications),
            stringResource(id = R.string.notifications)
          )
        }
      },
      actions = {
        IconButton(onClick = search) {
          Icon(
            painterResource(id = org.quran.ui.R.drawable.ic_search),
            stringResource(id = R.string.search_ayah)
          )
        }
        IconButton(onClick = navigateToMore) {
          Icon(
            painterResource(id = R.drawable.ic_more_vert),
            stringResource(id = R.string.more)
          )
        }
      },
      colors = topBarColor,
    )
    TabBar(
      selectedTabIndex = pagerState.currentPage,
      containerColor = Color.Transparent,
      indicator = { tabPositions ->
        TabRowDefaults.Indicator(
          Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
          height = 2.dp
        )
      }
    ) {
      tabItems.forEachIndexed { index, tab ->
        TabItem(
          selected = pagerState.currentPage == index,
          onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
          content = {
            Text(
              text = stringResource(tab),
              modifier = Modifier.padding(horizontal = 12.dp),
              style = MaterialTheme.typography.labelLarge
            )
          },
        )
      }
    }
  }
}
