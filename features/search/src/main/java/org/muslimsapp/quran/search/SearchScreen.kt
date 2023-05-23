package org.muslimsapp.quran.search

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import arg.quran.models.quran.VerseKey
import org.alquran.ui.components.LineSeparator
import org.alquran.ui.theme.quran
import org.quran.search.R
import org.quran.ui.components.BackButton
import org.quran.ui.utils.extensions.htmlToAnnotatedString

@Composable
internal fun SearchScreen(
  uiState: SearchUiState,
  navigateToQuranPage: (page: Int, suraAyah: VerseKey) -> Unit,
  popBackStack: () -> Unit,
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
  ) {
    SearchBar(
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentWidth(),
      query = uiState.query,
      onQueryChange = uiState.onQueryChange,
      onSearch = uiState.onQueryChange,
      active = true,
      onActiveChange = { },
      placeholder = { Text(stringResource(id = R.string.search_ayah)) },
      leadingIcon = { BackButton(onClick = popBackStack) },
      trailingIcon = {
        Crossfade(targetState = uiState.loading, label = "trailingIcon") { loading ->
          if (loading) {
            CircularProgressIndicator(
              modifier = Modifier
                .size(48.dp)
                .padding(14.dp),
              strokeWidth = 2.dp
            )
          } else {
            IconButton(onClick = { uiState.onQueryChange("") }) {
              Icon(
                painter = painterResource(id = R.drawable.clear),
                contentDescription = stringResource(id = R.string.clear_search)
              )
            }
          }
        }
      },
    ) {

      val matchView = @Composable {
        if (uiState.results.isNotEmpty()) {
          Text(uiState.results.size.toString(), style = MaterialTheme.typography.labelSmall)
        }
      }

      LazyRow(
        modifier = Modifier.wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 8.dp),
      ) {
        item {
          FilterChip(
            selected = uiState.selectedEdition == null,
            onClick = { uiState.setSelectedEdition(null) },
            label = { Text(stringResource(id = R.string.quran)) },
            trailingIcon = {
              if (uiState.selectedEdition == null) {
                matchView()
              }
            }
          )
        }

        items(uiState.translations) { edition ->
          FilterChip(
            selected = uiState.selectedEdition == edition.slug,
            onClick = { uiState.setSelectedEdition(edition.slug) },
            label = { Text(edition.name) },
            trailingIcon = {
              if (uiState.selectedEdition == edition.slug) {
                matchView()
              }
            }
          )
        }
      }

      LazyColumn(contentPadding = WindowInsets.navigationBars.asPaddingValues()) {

        itemsIndexed(
          items = uiState.results,
          key = { _, a -> "${a.sura}:${a.ayah}" }
        ) { index, item ->

          Column(modifier = Modifier.animateItemPlacement()) {
            if (index > 0) LineSeparator()
            SearchResultItem(uiState = item, modifier = Modifier.clickable {
              navigateToQuranPage(uiState.getPage(item), VerseKey(item.sura, item.ayah))
            })
          }
        }
      }
    }
  }
}

@Composable
fun SearchResultItem(
  uiState: SearchResult,
  modifier: Modifier = Modifier,
) {

  Column(modifier = modifier.padding(vertical = 16.dp, horizontal = 24.dp)) {

    Text(
      text = uiState.name,
      style = MaterialTheme.typography.labelLarge,
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = htmlToAnnotatedString(uiState.text),
      style = when (uiState.type) {
        SearchResult.Type.QURAN -> MaterialTheme.typography.quran
        SearchResult.Type.TRANSLATION -> LocalTextStyle.current
      },
      modifier = Modifier.fillMaxWidth(),
      maxLines = 2,
      overflow = TextOverflow.Ellipsis,
    )
  }
}