package org.quran.features.pager.components.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.alquran.ui.components.LineSeparator
import org.quran.features.pager.uiState.DialogUiState
import org.quran.features.pager.uiState.QuranEvent
import org.quran.ui.R

@Composable
fun VerseMenuSheet(
  uiState: DialogUiState.VerseMenu,
  onEvent: (QuranEvent) -> Unit,
  navigateToShare: () -> Unit,
  onDismissRequest: () -> Unit,
) {
  ModalBottomSheet(
    onDismissRequest = onDismissRequest,
    windowInsets = WindowInsets(0, 0, 0, 0),
  ) {
    ListItem(
      leadingContent = { Icon(painterResource(id = R.drawable.play), null) },
      headlineContent = { Text(stringResource(id = R.string.play)) },
      modifier = Modifier.clickable { onEvent(QuranEvent.Play(uiState.verse)) }
    )
    ListItem(
      leadingContent = { Icon(painterResource(id = R.drawable.outline_play), null) },
      headlineContent = { Text(stringResource(id = R.string.play_the_word)) },
      modifier = Modifier.clickable { onEvent(QuranEvent.Play(uiState.verse)) }
    )
    ListItem(
      leadingContent = { Icon(painterResource(id = R.drawable.repeat), null) },
      headlineContent = { Text(stringResource(id = R.string.repeat_ayah)) },
      modifier = Modifier.clickable { }
    )
    LineSeparator(modifier = Modifier.padding(start = 58.dp, end = 16.dp))
    ListItem(
      leadingContent = { Icon(painterResource(id = R.drawable.content_copy), null) },
      headlineContent = { Text(stringResource(id = R.string.copy)) },
      modifier = Modifier.clickable { }
    )
    LineSeparator(modifier = Modifier.padding(start = 58.dp, end = 16.dp))
    BookmarkListItem(
      isBookmarked = uiState.bookmarked,
      modifier = Modifier.clickable {
        onEvent(QuranEvent.ToggleBookmark(uiState.verse, uiState.bookmarked))
      }
    )

    LineSeparator(modifier = Modifier.padding(start = 58.dp, end = 16.dp))

    ListItem(
      leadingContent = { Icon(painterResource(id = R.drawable.ic_share), null) },
      headlineContent = { Text(stringResource(id = R.string.share)) },
      modifier = Modifier.clickable(onClick = navigateToShare)
    )

    Box(modifier = Modifier.navigationBarsPadding())
  }
}

@Composable
private fun BookmarkListItem(
  isBookmarked: Boolean,
  modifier: Modifier = Modifier,
) {
  val bookmarkDrawable = when {
    isBookmarked -> R.drawable.bookmark_added
    else -> R.drawable.bookmark_add
  }
  val stringRes = when {
    isBookmarked -> R.string.remove_bookmark
    else -> R.string.add_bookmark
  }
  ListItem(
    leadingContent = { Icon(painterResource(id = bookmarkDrawable), null) },
    headlineContent = { Text(stringResource(id = stringRes)) },
    modifier = modifier
  )
}