package org.alquran.ui.screen.pager.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.alquran.R

@Composable
fun AyaMenuSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberSheetState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = {
            coroutineScope.launch {
                sheetState.hide()
                onDismissRequest()
            }
        }
    ) {
        ListItem(
            leadingContent = { Icon(painterResource(id = R.drawable.ic_play_arrow), null) },
            headlineText = { Text(stringResource(id = R.string.play)) },
            modifier = Modifier.clickable {  }
        )
        ListItem(
            leadingContent = { Icon(painterResource(id = R.drawable.ic_repeat), null) },
            headlineText = { Text(stringResource(id = R.string.repeat_ayah)) },
            modifier = Modifier.clickable {  }
        )
        ListItem(
            leadingContent = { Icon(painterResource(id = R.drawable.ic_edit), null) },
            headlineText = { Text(stringResource(id = R.string.add_note)) },
            modifier = Modifier.clickable {  }
        )
        ListItem(
            leadingContent = { Icon(painterResource(id = R.drawable.ic_bookmark), null) },
            headlineText = { Text(stringResource(id = R.string.add_bookmark)) },
            modifier = Modifier.clickable {  }
        )
        ListItem(
            leadingContent = { Icon(painterResource(id = R.drawable.ic_share), null) },
            headlineText = { Text(stringResource(id = R.string.share)) },
            modifier = Modifier.clickable {  }
        )
    }
}