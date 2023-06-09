package org.quran.features.pager.components.pages

import android.content.res.Configuration
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.alquran.ui.components.LineSeparator
import org.quran.features.pager.components.ProviderPage77QuranTextStyle
import org.quran.features.pager.components.SurahHeader
import org.quran.features.pager.components.TranslationItemView
import org.quran.features.pager.components.VerseItemView
import org.quran.features.pager.components.VerseToolbarView
import org.quran.features.pager.previews.TranslationPageProvider
import org.quran.features.pager.uiState.PageItem
import org.quran.features.pager.uiState.QuranEvent
import org.quran.features.pager.uiState.TranslationPage
import org.quran.ui.theme.QuranTheme
import org.quran.ui.utils.extensions.add
import kotlin.math.min

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
    if (page.scrollIndex >= 1 && !page.firstItem) {
      listState.animateScrollToItem(page.scrollIndex)
    }
  }

  val interactionSource = remember { MutableInteractionSource() }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = { currentOnEvent(QuranEvent.AyahPressed) }
      ),
    contentPadding = WindowInsets.displayCutout.asPaddingValues().add(bottom = 16.dp),
    state = listState,
    verticalArrangement = spacedByWithFooter(0.dp),
  ) {

    item(key = "header") {
      PageHeaderView(header = page.header, modifier = Modifier.padding(8.dp))
    }

    items(page.items, contentType = { it::class.simpleName }, key = { it.key }) { row ->
      when (row) {
        is TranslationPage.Chapter -> SurahHeader(surahName = row.name, suraNumber = row.number)

        is TranslationPage.Verse -> VerseItemView(verse = row)

        is TranslationPage.Divider -> LineSeparator(Modifier.padding(horizontal = 8.dp))

        is TranslationPage.TranslatedVerse -> TranslationItemView(translation = row)

        is TranslationPage.VerseToolbar -> VerseToolbarView(verse = row, onEvent = currentOnEvent)
      }
    }

    item(key = "footer") {
      Text(
        text = page.page.toString(),
        modifier = Modifier.fillMaxWidth(),
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

@Composable
fun PageHeaderView(header: PageItem.Header, modifier: Modifier = Modifier) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 8.dp, horizontal = 12.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {

    Text(
      text = header.leading,
      style = MaterialTheme.typography.labelMedium
    )

    Text(
      text = header.trailing,
      textAlign = TextAlign.End,
      style = MaterialTheme.typography.labelMedium
    )
  }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun TranslationPagePreview(
  @PreviewParameter(TranslationPageProvider::class) page: TranslationPage
) {
  QuranTheme {
    Surface {
      ProviderPage77QuranTextStyle {
        TranslationPageItem(page = page) { }
      }
    }
  }
}