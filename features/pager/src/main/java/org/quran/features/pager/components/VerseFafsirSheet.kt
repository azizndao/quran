package org.quran.features.pager.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.quran.features.pager.uiState.DialogUiState
import org.quran.ui.R


@Composable
fun VerseTafsirSheet(
  uiState: DialogUiState.VerseTafsir,
  onDismissRequest: () -> Unit,
) {
  ModalBottomSheet(
    onDismissRequest = onDismissRequest,
    windowInsets = WindowInsets(0,0,0,0)
  ) {
    Box(modifier = Modifier.navigationBarsPadding())
  }
}

