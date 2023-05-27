package org.quran.features.pager.components.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.quran.features.pager.uiState.DialogUiState


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

