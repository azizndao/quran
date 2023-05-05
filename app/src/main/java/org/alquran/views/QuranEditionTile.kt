package org.alquran.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.alquran.R
import org.quran.ui.components.RadioButtonListItem
import org.quran.ui.theme.QuranTheme
import org.quram.common.extensions.capital
import org.quran.datastore.QuranEdition

@Composable
fun QuranEditionTile(
  modifier: Modifier = Modifier,
  selectedScript: QuranEdition,
  onSelect: (QuranEdition) -> Unit,
) {
  var showDialog by remember { mutableStateOf(false) }
  val colors = ListItemDefaults.colors(Color.Transparent)

  ListItem(
    modifier = modifier.clickable { showDialog = true },
    leadingContent = { Icon(painterResource(id = R.drawable.ic_code), null) },
    headlineContent = { Text(stringResource(id = R.string.script)) },
    supportingContent = { Text(selectedScript.name.capital()) },
    colors = colors,
  )

  if (!showDialog || QuranEdition.values().size == 1) return

  var tempScript by remember(selectedScript) { mutableStateOf(selectedScript) }

  AlertDialog(
    title = { Text(stringResource(R.string.arabic_text)) },
    icon = { Icon(painterResource(id = R.drawable.ic_code), null) },
    text = {
      Column {
        for (arabicText in QuranEdition.values()) {
          RadioButtonListItem(
            selected = arabicText == tempScript,
            onClick = { tempScript = arabicText }
          ) { Text(arabicText.name.capital()) }
        }
      }
    },
    onDismissRequest = { showDialog = false },
    confirmButton = {
      TextButton(onClick = { onSelect(tempScript); showDialog = false }) {
        Text(stringResource(id = R.string.confirm))
      }
    },
    dismissButton = {
      TextButton(onClick = { showDialog = false }) {
        Text(stringResource(id = R.string.cancel))
      }
    },
  )
}

@Preview(showBackground = true)
@Composable
fun Preview_QuranEditionTile() {
  QuranTheme {
    QuranEditionTile(selectedScript = QuranEdition.KING_FAHAD_COMPLEX_V1) {}
  }
}
