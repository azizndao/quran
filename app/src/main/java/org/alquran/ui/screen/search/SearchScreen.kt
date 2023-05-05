package org.alquran.ui.screen.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.alquran.R

@Composable
fun SearchScreen(
  uiState: SearchUiState,
  navigate: (String) -> Unit,
  popBackStack: () -> Unit,
) {
  Box {
    SearchBar(
      query = uiState.query,
      onQueryChange = uiState.onQueryChange,
      onSearch = uiState.onQueryChange,
      active = uiState.active,
      onActiveChange = uiState.onActiveChange,
      placeholder = { Text(stringResource(id = R.string.search_ayah)) },
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentWidth()
    ) {

    }
  }
}