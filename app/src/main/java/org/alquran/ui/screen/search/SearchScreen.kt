package org.alquran.ui.screen.search

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import org.alquran.R
import org.quran.ui.utils.extensions.toAnnotatedString

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
        .wrapContentWidth(),
      leadingIcon = {
        IconButton(
          onClick = {
            if (uiState.active) {
              uiState.onActiveChange(false)
            } else {
              popBackStack()
            }
          },
        ) {
          Icon(painterResource(id = R.drawable.ic_arrow_back), null)
        }
      },
      trailingIcon = {
        Crossfade(uiState) { state ->
          if (state.query.isEmpty()) {
            IconButton(onClick = { /*TODO*/ }) {
              Icon(painterResource(id = R.drawable.mic), null)
            }
          } else if (state.loading) {
            CircularProgressIndicator(modifier = Modifier.size(48.dp), strokeWidth = 2.dp)
          } else {
            IconButton(onClick = { uiState.onQueryChange("") }) {
              Icon(painterResource(id = R.drawable.ic_clear), null)
            }
          }
        }
      }) {

      Text("Result: " + uiState.results.size, style = MaterialTheme.typography.headlineLarge)

      val colorScheme = MaterialTheme.colorScheme
      val textStyle = MaterialTheme.typography.bodyMedium
      val arabicStyle = MaterialTheme.typography.bodyLarge.copy(
        textDirection = TextDirection.ContentOrRtl,
        textAlign = TextAlign.Right,
      )

      LazyColumn {
        for (result in uiState.results) {
          item(key = result.subtext) {

            val style = if (result.type == SearchResultType.LATIN) textStyle else arabicStyle

            val text = remember(result.text) {
              buildAnnotatedString {
                val html = HtmlCompat.fromHtml(result.text, HtmlCompat.FROM_HTML_MODE_COMPACT)
                append(html.toAnnotatedString(colorScheme, style))
              }
            }

            ListItem(
              headlineContent = { Text(text, style = style) },
              supportingContent = { Text(result.subtext) },
              modifier = Modifier.clickable {

              }
            )
          }
          item {
            Divider(modifier = Modifier.padding(horizontal = 16.dp))
          }
        }
      }
    }
  }
}