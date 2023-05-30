package org.quran.features.pager.components.pages

import android.content.res.Configuration
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import org.quran.features.pager.R
import org.quran.features.pager.components.ProviderPage604QuranTextStyle
import org.quran.features.pager.previews.QuranPageProvider
import org.quran.features.pager.uiState.QuranEvent
import org.quran.features.pager.uiState.QuranPage
import org.quran.ui.components.LongClickableText
import org.quran.ui.theme.LocalQuranTextStyle
import org.quran.ui.theme.QuranTheme
import org.quran.ui.theme.surahNames

@Composable
fun QuranPageView(
  page: QuranPage,
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
        header = page.header,
        modifier = Modifier
          .height(lineHeight)
          .fillMaxWidth()
      )
      Spacer(modifier = Modifier.weight(1f))

      for (line in page.lines) {
        when (line) {
          is QuranPage.Basmallah -> Image(
            painter = painterResource(id = R.drawable.bismillah),
            contentDescription = null,
            modifier = Modifier
              .fillMaxWidth()
              .height(lineHeight)
              .padding(vertical = 4.dp),
            contentScale = ContentScale.Inside,
            colorFilter = ColorFilter.tint(LocalContentColor.current)
          )

          is QuranPage.Blank -> Spacer(
            Modifier
              .height(lineHeight)
              .fillMaxWidth()
          )

          is QuranPage.ChapterLine -> QuranChapterLine(
            line = line,
            modifier = Modifier
              .height(lineHeight)
              .fillMaxWidth()
          )

          is QuranPage.TextLine -> QuranTextLine(
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
fun QuranChapterLine(
  line: QuranPage.ChapterLine,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
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
        .align(Alignment.Center),
    )
  }
}

@Composable
fun QuranTextLine(
  line: QuranPage.TextLine,
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


@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun QuranPageViewPreview(
  @PreviewParameter(QuranPageProvider::class) page: QuranPage,
) {
  QuranTheme {
    Surface {
      ProviderPage604QuranTextStyle {
        QuranPageView(page = page) { }
      }
    }
  }
}