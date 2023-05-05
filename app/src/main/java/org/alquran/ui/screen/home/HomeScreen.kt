package org.alquran.ui.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import org.alquran.R
import org.alquran.ui.components.TabBar
import org.alquran.ui.components.TabItem
import org.alquran.ui.components.pagerTabIndicatorOffset
import org.alquran.ui.screen.home.bookmarks.ListBookmark
import org.alquran.ui.screen.home.juzs.ListJuzs
import org.alquran.ui.screen.home.surahs.SurahList
import org.alquran.ui.screen.pager.directionToQuranPage
import org.alquran.ui.screen.search.directionToQuranSearch
import org.quran.ui.components.MuslimsTopAppBarDefaults


val tabItems = listOf(R.string.surats, R.string.juzs, R.string.my_quran)

@Composable
internal fun HomeScreen(
  viewModel: HomeViewModel,
  navigate: (String) -> Unit,
  navigateToMore: () -> Unit,
) {
  val pagerState = rememberPagerState()

  val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
    state = rememberTopAppBarState(),
  )

  Column(modifier = Modifier) {
    AppBar(
      scrollBehavior = scrollBehavior,
      pagerState = pagerState,
      search = { navigate(directionToQuranSearch()) },
      navigateToMore = navigateToMore,
    )
    HorizontalPager(state = pagerState, pageCount = tabItems.size) { position ->
      Box(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)) {
        when (position) {
          0 -> SurahList(
            uiStateFlow = viewModel.surahsFlow,
            onNavigate = { page, key -> navigate(directionToQuranPage(page, key)) }
          )

          1 -> ListJuzs(uiStateFlow = viewModel.hibzUiStateFlow) {
            navigate(directionToQuranPage(it.page, null))
          }

          2 -> ListBookmark(uiStateFlow = viewModel.bookmarksUiStateFlow) {
            navigate(directionToQuranPage(it.page, VerseKey(it.sura, it.ayah)))
          }

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
            painterResource(id = R.drawable.ic_notifications),
            stringResource(id = R.string.notifications)
          )
        }
      },
      actions = {
        IconButton(onClick = search) {
          Icon(
            painterResource(id = R.drawable.ic_search),
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
