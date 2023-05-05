package org.alquran.ui.screen.home.bookmarks

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow
import org.alquran.R
import org.muslimapp.feature.quran.ui.LocalInsetsPadding
import org.alquran.views.CircularProgressLoader
import org.alquran.views.SectionTitle
import org.alquran.views.sectionItems
import org.quran.bookmarks.model.BookmarkTag
import org.quran.datastore.QuranPosition

@ExperimentalMaterial3Api
@Composable
fun ListBookmark(
  modifier: Modifier = Modifier,
  uiStateFlow: StateFlow<ListBookmarksUiState>,
  onNavigate: (QuranPosition) -> Unit,
) {

  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

  val uiState by uiStateFlow.collectAsStateWithLifecycle()

  CircularProgressLoader(
    loading = uiState.loading,
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
  ) {
    when {
      uiState.bookmarks.isEmpty() -> EmptyBookmarkList()
      else -> ListBookmarks(
        modifier = modifier,
        uiState = uiState.bookmarks,
        onNavigate = onNavigate
      )
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ListBookmarks(
  modifier: Modifier = Modifier,
  listState: LazyListState = rememberLazyListState(),
  uiState: List<BookmarkUiModel>,
  onNavigate: (QuranPosition) -> Unit,
) {
  LazyColumn(
    modifier = modifier.fillMaxSize(),
    state = listState,
    contentPadding = LocalInsetsPadding.current
      .add(WindowInsets(bottom = 16.dp, top = 16.dp, left = 12.dp, right = 12.dp))
      .asPaddingValues()
  ) {

    uiState.groupBy { it.tag }.forEach { (tag, bookmarks) ->

      stickyHeader {
        SectionTitle {
          when (tag) {
            BookmarkTag.Bookmark -> Text(stringResource(id = R.string.bookmarks))
            is BookmarkTag.Custom -> Text(tag.name)
            is BookmarkTag.PopularAya -> Text(stringResource(id = R.string.popular_ayahs))
            is BookmarkTag.PopularSura -> Text(stringResource(id = R.string.popular_sura))
            BookmarkTag.Recent -> Text(stringResource(id = R.string.recent))
          }
        }
      }

      sectionItems(bookmarks) { bookmark ->
        BookmarkedAyahItem(
          bookmarkUiModel = bookmark,
          modifier = Modifier.clickable {
            onNavigate(
              QuranPosition(
                page = bookmark.page,
                sura = bookmark.key.sura,
                ayah = bookmark.key.aya
              )
            )
          }
        )
      }
    }
  }
}

@Composable
fun EmptyBookmarkList() {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .windowInsetsPadding(WindowInsets.navigationBars)
      .padding(bottom = 56.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    val color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)

    Icon(
      painter = painterResource(R.drawable.ic_bookmark),
      contentDescription = null,
      modifier = Modifier.size(32.dp),
      tint = color
    )
    Text(
      text = stringResource(id = R.string.nothing_found),
      style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
      color = color
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkedAyahItem(
  modifier: Modifier = Modifier,
  bookmarkUiModel: BookmarkUiModel,
) {

  ListItem(
    modifier = modifier,
    leadingContent = {
      Box(modifier = Modifier.size(42.dp)) {
        Icon(
          painter = when (bookmarkUiModel.tag) {
            BookmarkTag.Bookmark -> painterResource(id = R.drawable.ic_baseline_bookmark)
            is BookmarkTag.Custom -> painterResource(id = R.drawable.ic_favorite)
            is BookmarkTag.PopularAya, is BookmarkTag.PopularSura -> painterResource(id = R.drawable.ic_favorite)
            BookmarkTag.Recent -> painterResource(id = R.drawable.ic_history)
          },
          null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.align(Alignment.Center)
        )
      }
    },
    headlineContent = { Text(bookmarkUiModel.nameSimple) },
    supportingContent = { Text(stringResource(id = R.string.page_x, bookmarkUiModel.page)) },
  )
}


//@Preview(showBackground = true)
//@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
//@Composable
//fun BookmarkedAyahPreview(
//    @PreviewParameter(
//        AyahPreviewParameterProvider::class,
//        1
//    ) ayahUi: org.alquran.quran.models.Verse,
//) {
//    MuslimsTheme {
//
//        BookmarkedAyahItem(
//            bookmarkUiModel = BookmarkUiModel(
//                bookmarkId = 1,
//                "Al-Baqara",
//                ayahUi.sura,
//                ayahUi.ayah,
//                ayahUi.juz,
//                ayahUi.page,
//            ),
//        )
//    }
//}
