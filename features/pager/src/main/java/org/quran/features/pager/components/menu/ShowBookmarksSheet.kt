package org.quran.features.pager.components.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import arg.quran.models.quran.VerseKey
import org.quran.ui.components.LineSeparator
import org.quran.bookmarks.models.BookmarkTag
import org.quran.features.pager.uiState.DialogUiState
import org.quran.ui.R

@Composable
fun ShowBookmarksSheet(
  uiState: DialogUiState.ShowBookmarks,
  onDelete: (verse: VerseKey, tag: BookmarkTag) -> Unit,
  onDismissRequest: () -> Unit
) {
  ModalBottomSheet(
    onDismissRequest = onDismissRequest,
    windowInsets = WindowInsets(0, 0, 0, 0)
  ) {
    Text(
      stringResource(id = R.string.bookmark_tags),
      style = MaterialTheme.typography.titleLarge,
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp, start = 24.dp, end = 24.dp),
    )

    uiState.tags.forEachIndexed { index, tag ->

      if (index > 0) LineSeparator(Modifier.padding(end = 16.dp, start = 56.dp))

      ListItem(
        headlineContent = { Text(tag.name) },
        leadingContent = {
          val tint = tag.color?.let { Color(it) } ?: LocalContentColor.current
          Icon(painterResource(id = R.drawable.bookmark_added), null, tint = tint)
        },
        modifier = Modifier
          .padding(horizontal = 8.dp)
          .clip(CircleShape)
          .clickable { onDelete(uiState.verse, tag) },
        trailingContent = { Icon(painterResource(id = R.drawable.ic_delete), null) }
      )
    }

    Box(modifier = Modifier.navigationBarsPadding())
  }
}