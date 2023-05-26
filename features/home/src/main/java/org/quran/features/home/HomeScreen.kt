package org.quran.features.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import org.quran.ui.components.TabBar
import org.quran.ui.components.TabItem
import org.quran.ui.components.pagerTabIndicatorOffset
import org.quran.features.home.juzs.ListJuzs
import org.quran.features.home.surahs.SurahList
import org.quran.ui.components.MuslimsTopAppBarDefaults
import org.quran.ui.R as Ui


val tabItems = listOf(Ui.string.surats, Ui.string.juzs)

@Composable
internal fun HomeScreen(
  viewModel: HomeViewModel,
  contentPadding: PaddingValues,
  navigateToMore: () -> Unit,
  navigateToSearch: () -> Unit,
  navigateToPage: (page: Int, key: VerseKey?) -> Unit,
) {
  val pagerState = rememberPagerState { tabItems.size }

  val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
    state = rememberTopAppBarState(),
  )

  Column(modifier = Modifier) {
    AppBar(
      scrollBehavior = scrollBehavior,
      pagerState = pagerState,
      search = navigateToSearch,
      navigateToMore = navigateToMore,
    )
    HorizontalPager(state = pagerState) { position ->
      Box(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)) {
        when (position) {
          0 -> SurahList(
            contentPadding = contentPadding,
            uiStateFlow = viewModel.surahsFlow,
            onNavigate = { page, key -> navigateToPage(page, key) },
          )

          1 -> ListJuzs(
            contentPadding = contentPadding,
            uiStateFlow = viewModel.hibzUiStateFlow,
            onNavigate = { navigateToPage(it.page, null) }
          )

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
      title = { Text(stringResource(Ui.string.app_name)) },
      navigationIcon = {
        IconButton(onClick = { }) {
          Icon(
            painterResource(id = org.quran.ui.R.drawable.ic_notifications),
            stringResource(id = Ui.string.notifications)
          )
        }
      },
      actions = {
        IconButton(onClick = search) {
          Icon(
            painterResource(id = org.quran.ui.R.drawable.ic_search),
            stringResource(id = Ui.string.search_ayah)
          )
        }
        IconButton(onClick = navigateToMore) {
          Icon(
            painterResource(id = Ui.drawable.ic_more_vert),
            stringResource(id = Ui.string.more)
          )
        }
      },
      colors = topBarColor,
    )
    TabBar(
      selectedTabIndex = pagerState.currentPage,
      containerColor = Color.Transparent,
      indicator = { tabPositions ->
        TabRowDefaults.SecondaryIndicator(
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
