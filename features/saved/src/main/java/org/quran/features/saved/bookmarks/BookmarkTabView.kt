package org.quran.features.saved.bookmarks

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.PersistentList
import org.alquran.ui.components.LineSeparator
import org.alquran.ui.components.SurfaceCard
import org.quran.bookmarks.models.Bookmark
import org.quran.bookmarks.models.TabWithBookmarks
import org.quran.ui.R
import org.quran.ui.components.SectionTitle
import org.quran.ui.utils.extensions.add

@Composable
fun BookmarkTabView(
  contentPadding: PaddingValues,
  bookmarksTags: PersistentList<TabWithBookmarks>,
  listState: LazyListState = rememberLazyListState(),
  onBookmarkClick: (Bookmark) -> Unit
) {
  val topShape = MaterialTheme.shapes.large.copy(
    bottomEnd = CornerSize(0),
    bottomStart = CornerSize(0)
  )
  val bottomShape = MaterialTheme.shapes.large.copy(
    topEnd = CornerSize(0),
    topStart = CornerSize(0)
  )

  LazyColumn(
    state = listState,
    contentPadding = contentPadding.add(vertical = 12.dp, horizontal = 16.dp)
  ) {

    for (tag in bookmarksTags) {

      item {
        SectionTitle(modifier = Modifier.clip(topShape)) {
          Text(tag.tag.name)
        }
      }

      tag.bookmarks.forEachIndexed { index, bookmark ->

        if (index > 0) item {
          LineSeparator(Modifier.padding(horizontal = 16.dp))
        }

        item {
          SurfaceCard(
            shape = if (index < tag.bookmarks.size - 1) RectangleShape else bottomShape,
            onClick = { onBookmarkClick(bookmark) },
          ) {
            ListItem(
              headlineContent = { Text(text = bookmark.label) },
              leadingContent = { Icon(painterResource(R.drawable.bookmark_added), null) },
            )
          }
        }
      }
    }
  }
}