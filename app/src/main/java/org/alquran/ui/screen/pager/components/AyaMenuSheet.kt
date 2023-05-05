package org.alquran.ui.screen.pager.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.alquran.R
import org.alquran.ui.screen.pager.AyahEvent
import org.alquran.ui.uistate.PagerUiState
import org.alquran.ui.uistate.Selection

@Composable
fun AyaMenuSheet(
  uiState: PagerUiState,
  modifier: Modifier = Modifier,
  sheetState: SheetState = rememberModalBottomSheetState(),
  coroutineScope: CoroutineScope = rememberCoroutineScope(),
  onDismissRequest: () -> Unit,
) {
  val (key, bookmarked) = uiState.selection as Selection.Highlight

  val currentAyahEvent by rememberUpdatedState { event: AyahEvent ->
    uiState.onAyahEvent(event)
    coroutineScope.launch {
      sheetState.hide()
      onDismissRequest()
    }
  }
  ModalBottomSheet(
    modifier = modifier,
    sheetState = sheetState,
    onDismissRequest = {
      coroutineScope.launch {
        sheetState.hide()
        onDismissRequest()
      }
    }
  ) {
    ListItem(
      leadingContent = { Icon(painterResource(id = R.drawable.ic_play_arrow), null) },
      headlineContent = { Text(stringResource(id = R.string.play)) },
      modifier = Modifier.clickable { currentAyahEvent(AyahEvent.Play(key)) }
    )
    ListItem(
      leadingContent = { Icon(painterResource(id = R.drawable.ic_repeat), null) },
      headlineContent = { Text(stringResource(id = R.string.repeat_ayah)) },
      modifier = Modifier.clickable { }
    )
    ListItem(
      leadingContent = { Icon(painterResource(id = R.drawable.ic_edit), null) },
      headlineContent = { Text(stringResource(id = R.string.add_note)) },
      modifier = Modifier.clickable { }
    )
    ListItem(
      leadingContent = {
        Icon(
          painterResource(
            id = when {
              bookmarked -> R.drawable.bookmark_added
              else -> R.drawable.bookmark_add
            }
          ), null
        )
      },
      headlineContent = {
        Text(
          stringResource(
            id = when {
              bookmarked -> R.string.remove_bookmark
              else -> R.string.add_bookmark
            }
          )
        )
      },
      modifier = Modifier.clickable {
        currentAyahEvent(AyahEvent.ToggleBookmark(key, bookmarked))
      }
    )
    ListItem(
      leadingContent = { Icon(painterResource(id = R.drawable.ic_share), null) },
      headlineContent = { Text(stringResource(id = R.string.share)) },
      modifier = Modifier.clickable { }
    )
  }
}