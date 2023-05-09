package org.alquran.ui.screen.translations

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.alquran.R
import org.alquran.ui.components.LineSeparator
import org.alquran.ui.components.SurfaceCard
import org.alquran.utils.getLanguageFlag
import org.alquran.views.SectionTitle
import org.quran.datastore.LocaleTranslation
import org.quran.ui.components.BackButton
import org.quran.ui.components.MuslimsTopAppBarDefaults
import org.quran.ui.utils.extensions.add

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun TranslationsScreen(
  uiState: TranslationsListUiState,
  setTranslation: (TranslationUiState) -> Unit,
  popBackStack: () -> Unit,
  refresh: (Boolean) -> Unit,
) {

  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

  val snackbarHost = remember { SnackbarHostState() }

  Scaffold(
    topBar = {
      TopAppBar(
        navigationIcon = { BackButton(onClick = popBackStack) },
        title = { Text(stringResource(R.string.translations)) },
        scrollBehavior = scrollBehavior,
        windowInsets = WindowInsets.statusBars,
        colors = MuslimsTopAppBarDefaults.smallTopAppBarColors()
      )
    },
    modifier = Modifier
      .fillMaxSize()
      .nestedScroll(scrollBehavior.nestedScrollConnection),
    snackbarHost = { SnackbarHost(hostState = snackbarHost) },
  ) { innerPadding ->

    LaunchedEffect(uiState) {
      uiState.exception?.let { it1 -> snackbarHost.showSnackbar(it1) }
    }

    val refreshState = rememberPullRefreshState(
      uiState.loading,
      { refresh(true) },
      refreshingOffset = innerPadding.calculateTopPadding() + 56.dp,
      refreshThreshold = innerPadding.calculateTopPadding() + 80.dp,
    )

    Box(
      modifier = Modifier
        .fillMaxSize()
        .pullRefresh(refreshState),
    ) {

      TranslationList(
        uiState = uiState,
        contentPadding = innerPadding,
        onChange = setTranslation
      )

      PullRefreshIndicator(uiState.loading, refreshState, Modifier.align(Alignment.TopCenter))
    }
  }
}

@ExperimentalFoundationApi
@Composable
fun TranslationList(
  uiState: TranslationsListUiState,
  contentPadding: PaddingValues,
  onChange: (TranslationUiState) -> Unit,
) {

  val editions = uiState.editions

  LazyColumn(
    contentPadding = contentPadding
      .add(start = 12.dp, end = 12.dp, top = 16.dp, bottom = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      SurfaceCard(shape = MaterialTheme.shapes.large) {
        Column {
          SectionTitle { Text(stringResource(id = R.string.selected_translations)) }

          uiState.selectedTranslations.forEachIndexed { index, translation ->

            if (index != 0) LineSeparator(startIndent = 70.dp)

            SelectedTranslationItem(
              edition = translation,
              onClickItem = { }
            )
          }
        }
      }
    }

    editions.forEach { (displayLanguage, trans) ->
      item {
        SurfaceCard(shape = MaterialTheme.shapes.large) {
          Column {
            SectionTitle { Text(displayLanguage) }

            trans.forEachIndexed { index, translation ->

              if (index != 0) LineSeparator(startIndent = 70.dp)

              TranslationItem(
                edition = translation,
                onClickItem = onChange
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun SelectedTranslationItem(
  edition: LocaleTranslation,
  onClickItem: (LocaleTranslation) -> Unit,
) {

  val icon = @Composable {
    AsyncImage(
      model = getLanguageFlag(edition.languageCode),
      contentDescription = null,
      modifier = Modifier
        .size(32.dp)
        .clip(CircleShape),
      contentScale = ContentScale.Crop,
      error = painterResource(id = R.drawable.ic_language),
      placeholder = painterResource(id = R.drawable.ic_language),
    )
  }

  ListItem(
    leadingContent = icon,
    headlineContent = { Text(edition.name) },
    supportingContent = { Text(edition.authorName) },
    trailingContent = {
      Icon(
        painter = painterResource(id = R.drawable.baseline_reorder_24),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.secondary,
      )
    },
    modifier = Modifier.clickable { onClickItem(edition) },
  )
}

@Composable
fun TranslationItem(
  edition: TranslationUiState,
  onClickItem: (TranslationUiState) -> Unit,
) {

  val icon = @Composable {
    AsyncImage(
      model = getLanguageFlag(edition.languageCode),
      contentDescription = null,
      modifier = Modifier
        .size(32.dp)
        .clip(CircleShape),
      contentScale = ContentScale.Crop,
      error = painterResource(id = R.drawable.ic_language),
      placeholder = painterResource(id = R.drawable.ic_language),
    )
  }

  val trailing = @Composable {
    when {
      edition.downloaded && edition.selected -> Icon(
        painter = painterResource(id = R.drawable.ic_radio_button_checked),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.secondary,
      )

      edition.downloaded -> Icon(
        painter = painterResource(id = R.drawable.ic_radio_button_unchecked),
        contentDescription = null,
      )

      else -> Icon(painterResource(id = R.drawable.ic_download), null)
    }
  }

  ListItem(
    leadingContent = icon,
    headlineContent = { Text(edition.name) },
    supportingContent = { Text(edition.authorName) },
    trailingContent = trailing,
    modifier = Modifier.clickable { onClickItem(edition) },
  )
}
