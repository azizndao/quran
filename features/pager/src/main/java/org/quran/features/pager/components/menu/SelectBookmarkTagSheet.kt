package org.quran.features.pager.components.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import arg.quran.models.quran.VerseKey
import org.alquran.ui.components.LineSeparator
import org.quran.features.pager.uiState.DialogUiState
import org.quran.ui.R

@Composable
fun SelectBookmarkTagSheet(
  uiState: DialogUiState.SelectBookmarkTab,
  onCreate: (verse: VerseKey, tagName: String) -> Unit,
  onSelect: (verse: VerseKey, tagId: Int) -> Unit,
  onDismissRequest: () -> Unit,
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

      if (index > 0) LineSeparator(Modifier.padding(start = 60.dp, end = 16.dp))

      ListItem(
        headlineContent = { Text(tag.name) },
        leadingContent = {
          val tint = tag.color?.let { Color(it) } ?: LocalContentColor.current
          Icon(painterResource(id = R.drawable.bookmarks), null, tint = tint)
        },
        modifier = Modifier
          .padding(horizontal = 8.dp)
          .clip(CircleShape)
          .clickable { onSelect(uiState.verse, tag.id) }
      )
    }

    val (name, setName) = remember { mutableStateOf("") }

    TextField(
      value = name,
      onValueChange = setName,
      modifier = Modifier
        .padding(horizontal = 24.dp, vertical = 8.dp)
        .fillMaxWidth(),
      placeholder = { Text(text = stringResource(id = R.string.create_tag)) },
      trailingIcon = {
        IconButton(onClick = { onCreate(uiState.verse, name) }, enabled = name.isNotEmpty()) {
          Icon(painterResource(R.drawable.circle_up), null)
        }
      },
      keyboardActions = KeyboardActions(onDone = { onCreate(uiState.verse, name) }),
      keyboardOptions = KeyboardOptions(
        autoCorrect = true,
        capitalization = KeyboardCapitalization.Sentences,
        imeAction = ImeAction.Done
      )
    )

    Box(modifier = Modifier.navigationBarsPadding())
  }
}