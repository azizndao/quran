package org.quran.features.pager.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.alquran.ui.components.LineSeparator
import org.quran.features.pager.uiState.QuranEvent
import org.quran.features.pager.uiState.QuranPageItem
import org.quran.features.pager.uiState.TranslationPage
import org.quran.ui.utils.extensions.add
import kotlin.math.min
import org.quran.ui.R as UI

@Composable
fun TranslationPageItem(
  page: TranslationPage,
  modifier: Modifier = Modifier,
  onEvent: (QuranEvent) -> Unit,
) {

  val currentOnEvent by rememberUpdatedState(onEvent)

  val scrollOffset = -with(LocalDensity.current) { 64.dp.roundToPx() }

  val listState = rememberLazyListState(
    initialFirstVisibleItemIndex = if (page.scrollIndex < 0) 0 else page.scrollIndex,
    initialFirstVisibleItemScrollOffset = scrollOffset
  )

  LaunchedEffect(page) {
    if (page.scrollIndex >= 0) {
      listState.animateScrollToItem(page.scrollIndex)
    }
  }

  val interactionSource = remember { MutableInteractionSource() }

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = WindowInsets.displayCutout.asPaddingValues()
      .add(bottom = 16.dp),
    state = listState,
    verticalArrangement = spacedByWithFooter(0.dp),
  ) {

    item(key = "header") {
      PageHeaderView(
        header = page.header,
        modifier = Modifier.padding(12.dp)
      )
    }

    for (row in page.items) {
      when (row) {
        is TranslationPage.Chapter -> item(key = row.key, contentType = "surah") {
          SurahHeader(
            surahName = row.name,
            suraNumber = row.number,
            modifier = Modifier.clickable(
              interactionSource,
              indication = null,
              onClick = { currentOnEvent(QuranEvent.AyahPressed) }
            )
          )
        }

        is TranslationPage.Verse -> item(key = row.key, contentType = "verse") {
          VerseItemView(
            verse = row,
            modifier = Modifier.clickable(
              interactionSource,
              indication = null,
              onClick = { currentOnEvent(QuranEvent.AyahPressed) }
            )
          )
        }

        is TranslationPage.Divider -> item(key = row.key) {
          LineSeparator(
            modifier = Modifier.padding(horizontal = 16.dp),
          )
        }

        is TranslationPage.TranslatedVerse -> item(key = row.key, contentType = "translation") {
          TranslationItemView(
            translation = row,
            modifier = Modifier.clickable(
              interactionSource,
              indication = null,
              onClick = { currentOnEvent(QuranEvent.AyahPressed) }
            )
          )
        }

        is TranslationPage.VerseToolbar -> item(row.key, contentType = "toolbar") {
          VerseToolbarView(
            verse = row, onEvent = currentOnEvent,
            modifier = Modifier.clickable(
              interactionSource,
              indication = null,
              onClick = { currentOnEvent(QuranEvent.AyahPressed) }
            )
          )
        }
      }
    }

    item(key = "footer") {
      Text(
        page.page.toString(),
        modifier = Modifier
          .fillMaxWidth(),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.labelLarge
      )
    }
  }
}


fun spacedByWithFooter(space: Dp) = object : Arrangement.Vertical {

  override val spacing = space

  override fun Density.arrange(
    totalSize: Int,
    sizes: IntArray,
    outPositions: IntArray,
  ) {
    if (sizes.isEmpty()) return
    val spacePx = space.roundToPx()

    var occupied = 0
    var lastSpace = 0

    sizes.forEachIndexed { index, size ->

      if (index == sizes.lastIndex) {
        outPositions[index] = totalSize - size
      } else {
        outPositions[index] = min(occupied, totalSize - size)
      }
      lastSpace = min(spacePx, totalSize - outPositions[index] - size)
      occupied = outPositions[index] + size + lastSpace
    }
    occupied -= lastSpace
  }
}

//@Preview(showBackground = true)
//@Composable
//fun PageItemPreview(
//    @PreviewParameter(PageItemPreviewParameterProvider::class) page: TranslationPage,
//) {
//    MuslimsTheme {
//        Surface {
//            TranslationPageItem(page = page) {}
//        }
//    }
//}
//

@Composable
fun PageHeaderView(header: QuranPageItem.Header, modifier: Modifier = Modifier) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 8.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {

    Text(
      stringResource(UI.string.surah, header.leading),
      style = MaterialTheme.typography.bodyMedium
    )

    Text(
      text = header.trailing,
      textAlign = TextAlign.End,
      style = MaterialTheme.typography.bodySmall
    )
  }
}