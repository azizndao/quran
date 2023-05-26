package org.quran.features.pager.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import org.quran.ui.theme.LocalQuranTextStyle
import org.quran.ui.theme.surahNames
import org.quran.features.pager.uiState.QuranEvent
import org.quran.features.pager.R
import org.quran.features.pager.uiState.MushafPage
import org.quran.ui.components.LongClickableText

@Composable
fun MushafPageView(
  page: MushafPage,
  modifier: Modifier = Modifier,
  onAyahEvent: (QuranEvent) -> Unit
) {

  BoxWithConstraints(
    modifier = modifier
      .displayCutoutPadding()
      .fillMaxSize()
  ) {

    val lineHeight = with(LocalDensity.current) {
      LocalQuranTextStyle.current.lineHeight.toDp()
    }

    Column(
      modifier = Modifier
        .fillMaxSize(),
    ) {

      PageHeaderView(
        header = page.header, modifier = Modifier
          .height(lineHeight)
          .fillMaxWidth()
      )
      Spacer(modifier = Modifier.weight(1f))

      for (line in page.lines) {
        when (line) {
          is MushafPage.Basmallah -> Image(
            painter = painterResource(id = R.drawable.bismillah),
            contentDescription = null,
            modifier = Modifier
              .fillMaxWidth()
              .height(lineHeight)
              .padding(vertical = 4.dp),
            contentScale = ContentScale.Inside,
            colorFilter = ColorFilter.tint(LocalContentColor.current)
          )

          is MushafPage.Blank -> Spacer(
            Modifier
              .height(lineHeight)
              .fillMaxWidth()
          )

          is MushafPage.ChapterLine -> MushafChapterLine(
            line = line,
            modifier = Modifier
              .height(lineHeight)
              .fillMaxWidth()
          )

          is MushafPage.TextLine -> MushafTextLine(
            line = line,
            modifier = Modifier
              .height(lineHeight)
              .fillMaxWidth(),
            onAyahEvent = onAyahEvent,
          )
        }
      }

      Spacer(modifier = Modifier.weight(1f))

      Text(
        page.page.toString(),
        modifier = Modifier
          .heightIn(lineHeight)
          .fillMaxWidth()
          .wrapContentHeight(),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.labelLarge
      )
    }
  }
}

@Composable
fun MushafChapterLine(
  line: MushafPage.ChapterLine,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .paint(
        painterResource(id = R.drawable.surah_border),
        contentScale = ContentScale.Fit,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
      )
  ) {

    Text(
      text = "${line.sura.toString().padStart(3, '0')} surah",
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.surahNames,
      modifier = Modifier
        .requiredWidthIn()
        .align(Alignment.Center),
    )
  }
}

@Composable
fun MushafTextLine(
  line: MushafPage.TextLine,
  modifier: Modifier = Modifier,
  style: TextStyle = LocalQuranTextStyle.current,
  colorScheme: ColorScheme = MaterialTheme.colorScheme,
  onAyahEvent: (QuranEvent) -> Unit,
) {

  val text = remember(line) {
    buildAnnotatedString {
      line.words.forEach { word ->
        val color = when {
          word.playing -> colorScheme.surfaceColorAtElevation(2.dp)
          word.selected -> colorScheme.surfaceVariant
          word.bookmarked -> colorScheme.surfaceColorAtElevation(3.dp)
          else -> colorScheme.surface
        }

        withStyle(
          SpanStyle(
            background = color,
            color = colorScheme.contentColorFor(backgroundColor = color)
          )
        ) {
          append(word.text)
        }
      }
    }
  }

  LongClickableText(
    text = text,
    style = style,
    modifier = modifier
      .fillMaxWidth()
      .wrapContentWidth(),
    onClick = { onAyahEvent(QuranEvent.AyahPressed) },
    onLongClick = { offset ->
      val word = if (offset >= line.words.size) line.words.last() else line.words[offset]
      onAyahEvent(QuranEvent.AyahLongPressed(word.key, word.position, word.bookmarked))
    },
  )
}
