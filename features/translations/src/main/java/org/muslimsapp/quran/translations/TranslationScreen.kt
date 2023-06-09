package org.muslimsapp.quran.translations

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.GlideImage
import org.alquran.ui.components.LineSeparator
import org.alquran.ui.components.SurfaceCard
import org.alquran.utils.getLanguageFlag
import org.quram.common.extensions.capital
import org.quran.ui.R
import org.quran.ui.components.BackButton
import org.quran.ui.components.MuslimsTopAppBarDefaults
import org.quran.ui.components.NiaLoadingWheel
import org.quran.ui.components.SectionTitle
import org.quran.ui.utils.extensions.add
import java.util.Locale

@Composable
internal fun TranslationsScreen(
  uiState: TranslationsListUiState,
  popBackStack: () -> Unit,
) {

  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

  val snackbarHost = remember { SnackbarHostState() }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .nestedScroll(scrollBehavior.nestedScrollConnection),
  ) {
    Crossfade(
      targetState = uiState.selectedTranslation,
      label = "TranslationsScreen"
    ) { translation ->
      when (translation) {
        null -> TopAppBar(
          navigationIcon = { BackButton(onClick = popBackStack) },
          title = { Text(stringResource(R.string.translations)) },
          scrollBehavior = scrollBehavior,
          windowInsets = WindowInsets.statusBars,
          colors = MuslimsTopAppBarDefaults.smallTopAppBarColors()
        )

        else -> TopAppBar(
          navigationIcon = {
            IconButton(onClick = { uiState.clearSelection() }) {
              Icon(painterResource(id = R.drawable.ic_clear), null)
            }
          },
          title = { },
          scrollBehavior = scrollBehavior,
          windowInsets = WindowInsets.statusBars,
          colors = MuslimsTopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
          ),
          actions = {
            IconButton(onClick = { uiState.moveUp() }) {
              Icon(painterResource(id = R.drawable.circle_up), null)
            }
            IconButton(onClick = { uiState.moveDown() }) {
              Icon(painterResource(id = R.drawable.circle_down), null)
            }
            IconButton(onClick = { uiState.delete() }) {
              Icon(painterResource(id = R.drawable.ic_delete), null)
            }
          }
        )
      }
    }

    LaunchedEffect(uiState) {
      uiState.exception?.let { it1 -> snackbarHost.showSnackbar(it1) }
    }

    TranslationList(uiState = uiState)

    if (uiState.loading) {
      NiaLoadingWheel(
        modifier = Modifier
          .fillMaxWidth()
          .wrapContentWidth()
      )
    }
  }
}

@ExperimentalFoundationApi
@Composable
fun TranslationList(
  uiState: TranslationsListUiState,
  contentPadding: PaddingValues = PaddingValues(),
) {

  val topShape = MaterialTheme.shapes.extraLarge.copy(
    bottomEnd = CornerSize(0),
    bottomStart = CornerSize(0)
  )
  val bottomShape = MaterialTheme.shapes.extraLarge.copy(
    topEnd = CornerSize(0),
    topStart = CornerSize(0)
  )

  val colorScheme = MaterialTheme.colorScheme

  LazyColumn(contentPadding = contentPadding.add(vertical = 16.dp, horizontal = 12.dp)) {

    if (uiState.downloadedTranslations.isNotEmpty()) {
      item {
        SectionTitle(modifier = Modifier.clip(topShape)) {
          Text(stringResource(id = R.string.downloaded))
        }
      }

      uiState.downloadedTranslations.forEachIndexed { index, translation ->
        if (index != 0) item {
          LineSeparator(
            modifier = Modifier
              .background(colorScheme.surface)
              .padding(start = 70.dp)
          )
        }

        val shape =
          if (index < uiState.downloadedTranslations.size - 1) RectangleShape else bottomShape

        item {
          SurfaceCard(
            shape = shape,
            colors = CardDefaults.cardColors(containerColor = if (translation.selected) colorScheme.surfaceVariant else colorScheme.surface)
          ) {
            SelectedTranslationItem(
              edition = translation,
              modifier = Modifier.combinedClickable(
                onClick = {},
                onLongClick = { translation.onLongClick() },
                indication = rememberRipple(),
                interactionSource = remember { MutableInteractionSource() },
              ),
            )
          }
        }
      }
    }

    for ((displayLanguage, editions) in uiState.locales) {

      item { Spacer(modifier = Modifier.height(16.dp)) }

      item {
        SectionTitle(modifier = Modifier.clip(topShape)) {
          Text(displayLanguage)
        }
      }

      editions.forEachIndexed { index, translation ->
        if (index != 0) item { LineSeparator(startIndent = 70.dp) }

        val shape = if (index < editions.size - 1) RectangleShape else bottomShape

        item {
          SurfaceCard(shape = shape) {
            TranslationItem(
              edition = translation,
              modifier = Modifier.clickable(onClick = translation.onClick),
            )
          }
        }
      }
    }
  }
}

@Composable
fun TranslationItem(
  edition: TranslationUiState,
  modifier: Modifier = Modifier,
) {

  val icon = @Composable {
    GlideImage(
      model = getLanguageFlag(edition.languageCode),
      contentDescription = null,
      modifier = Modifier
        .size(32.dp)
        .clip(CircleShape),
      contentScale = ContentScale.Crop,
    ) {
      it.placeholder(R.drawable.ic_language)
        .error(R.drawable.ic_language)
    }
  }

  val trailing = @Composable {
    when {
      edition.downloaded && edition.enabled -> Icon(
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
//    supportingContent = { Text(edition.authorName) },
    trailingContent = trailing,
    modifier = modifier,
  )
}


@Composable
fun SelectedTranslationItem(
  edition: TranslationUiState,
  modifier: Modifier = Modifier,
) {

  val leadingContent = @Composable {
    GlideImage(
      model = getLanguageFlag(edition.languageCode),
      contentDescription = null,
      modifier = Modifier
        .size(32.dp)
        .clip(CircleShape),
      contentScale = ContentScale.Crop,
    ) {
      it.placeholder(R.drawable.ic_language).error(R.drawable.ic_language)
    }
  }

  val background by animateColorAsState(
    if (edition.selected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface,
    label = "SelectedTranslationItem"
  )

  val displayLanguage = remember(edition) { Locale(edition.languageCode).displayLanguage.capital() }
  ListItem(
    modifier = modifier,
    leadingContent = leadingContent,
    headlineContent = { Text(displayLanguage) },
    supportingContent = { Text(edition.name) },
    colors = ListItemDefaults.colors(containerColor = background),
  )
}
