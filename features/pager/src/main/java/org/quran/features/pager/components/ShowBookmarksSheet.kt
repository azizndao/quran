package org.quran.features.pager.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import arg.quran.models.quran.VerseKey
import org.alquran.ui.components.LineSeparator
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
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 24.dp, start = 24.dp, end = 24.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      Text(stringResource(id = R.string.bookmark_tags), style = MaterialTheme.typography.titleLarge)

      uiState.tags.forEachIndexed { index, tag ->

        if (index > 0) LineSeparator(Modifier.padding(horizontal = 16.dp))

        ListItem(
          headlineContent = { Text(tag.name) },
          leadingContent = {
            val tint = tag.color?.let { Color(it) } ?: LocalContentColor.current
            Icon(painterResource(id = R.drawable.bookmarks), null, tint = tint)
          },
          modifier = Modifier.clickable {
            onDelete(uiState.verse, tag)
          }
        )
      }
    }
    Box(modifier = Modifier.navigationBarsPadding())
  }
}