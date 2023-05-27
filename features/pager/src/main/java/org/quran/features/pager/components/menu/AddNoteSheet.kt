package org.quran.features.pager.components.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import arg.quran.models.quran.VerseKey
import org.quran.ui.R

@Composable
fun AddNoteBottomSheet(
  verse: VerseKey,
  onCreate: (verse: VerseKey, note: String) -> Unit,
  onDismissRequest: () -> Unit,
) {
  ModalBottomSheet(
    onDismissRequest = onDismissRequest,
    windowInsets = WindowInsets(0, 0, 0, 0)
  ) {
    val (note, setNote) = remember { mutableStateOf("") }
    Text(
      text = stringResource(id = R.string.add_note),
      style = MaterialTheme.typography.titleLarge,
      modifier = Modifier
        .padding(start = 24.dp, bottom = 16.dp, end = 24.dp)
        .fillMaxWidth()
    )
    TextField(
      value = note,
      onValueChange = setNote,
      modifier = Modifier
        .padding(start = 24.dp, bottom = 24.dp, end = 24.dp)
        .fillMaxWidth(),
      placeholder = { Text(text = stringResource(id = R.string.add_note)) },
      trailingIcon = {
        IconButton(onClick = { /*TODO*/ }, enabled = note.isNotEmpty()) {
          Icon(painterResource(R.drawable.circle_up), null)
        }
      },
      keyboardActions = KeyboardActions(onDone = { onCreate(verse, note) }),
      keyboardOptions = KeyboardOptions(
        autoCorrect = true,
        capitalization = KeyboardCapitalization.Sentences,
        imeAction = ImeAction.Done
      )
    )
    Box(modifier = Modifier.navigationBarsPadding())
  }
}