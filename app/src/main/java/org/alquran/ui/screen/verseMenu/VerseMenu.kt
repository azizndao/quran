package org.alquran.ui.screen.verseMenu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.alquran.R
import org.alquran.ui.components.LineSeparator

@Composable
fun VerseMenuSheet(
  uiState: VerseMenuUiState,
  navigate: (String) -> Unit,
  popBackStack: () -> Unit,
) {
  val currentAyahEvent by rememberUpdatedState { event: VerseMenuEvent ->
    uiState.onEvent(event)
    popBackStack()
  }
  Surface(tonalElevation = 1.dp) {
    Column(
      modifier = Modifier.navigationBarsPadding()
    ) {
      BottomSheetDefaults.DragHandle(
        modifier = Modifier
          .fillMaxWidth()
          .wrapContentWidth()
      )
      ListItem(
        leadingContent = { Icon(painterResource(id = R.drawable.play), null) },
        headlineContent = { Text(stringResource(id = R.string.play)) },
        modifier = Modifier.clickable { currentAyahEvent(VerseMenuEvent.Play) }
      )
      ListItem(
        leadingContent = { Icon(painterResource(id = R.drawable.outline_play), null) },
        headlineContent = { Text(stringResource(id = R.string.play_the_word)) },
        modifier = Modifier.clickable { currentAyahEvent(VerseMenuEvent.PlayWord) }
      )
      ListItem(
        leadingContent = { Icon(painterResource(id = R.drawable.repeat), null) },
        headlineContent = { Text(stringResource(id = R.string.repeat_ayah)) },
        modifier = Modifier.clickable { }
      )
      LineSeparator()
      ListItem(
        leadingContent = { Icon(painterResource(id = R.drawable.menu_book), null) },
        headlineContent = { Text(stringResource(id = R.string.tafsir)) },
        modifier = Modifier.clickable { }
      )
      LineSeparator()
      ListItem(
        leadingContent = { Icon(painterResource(id = R.drawable.copy_all), null) },
        headlineContent = { Text(stringResource(id = R.string.copy)) },
        modifier = Modifier.clickable { }
      )
      ListItem(
        leadingContent = { Icon(painterResource(id = R.drawable.content_copy), null) },
        headlineContent = { Text(stringResource(id = R.string.advanced_copy)) },
        modifier = Modifier.clickable { }
      )
      LineSeparator()
      ListItem(
        leadingContent = { Icon(painterResource(id = R.drawable.edit), null) },
        headlineContent = { Text(stringResource(id = R.string.add_note)) },
        modifier = Modifier.clickable { }
      )
      val bookmarkDrawable = when {
        uiState.isBookmarked -> R.drawable.bookmark_added
        else -> R.drawable.bookmark_add
      }
      val stringRes = when {
        uiState.isBookmarked -> R.string.remove_bookmark
        else -> R.string.add_bookmark
      }
      ListItem(
        leadingContent = { Icon(painterResource(id = bookmarkDrawable), null) },
        headlineContent = { Text(stringResource(id = stringRes)) },
        modifier = Modifier.clickable { currentAyahEvent(VerseMenuEvent.Bookmark) }
      )
      LineSeparator()
      ListItem(
        leadingContent = { Icon(painterResource(id = R.drawable.ic_share), null) },
        headlineContent = { Text(stringResource(id = R.string.share_text)) },
        modifier = Modifier.clickable { }
      )
      ListItem(
        leadingContent = { Icon(painterResource(id = R.drawable.ic_share), null) },
        headlineContent = { Text(stringResource(id = R.string.share_image)) },
        modifier = Modifier.clickable { }
      )

    }
  }
}