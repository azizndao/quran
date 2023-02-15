package org.alquran.ui.screen.pager.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.alquran.R
import org.alquran.ui.screen.pager.AyahEvent
import org.alquran.ui.theme.LocalQuranTextStyle
import org.alquran.ui.theme.surahNames
import org.alquran.ui.uistate.MushafPage
import org.quran.network.model.CharType

@Composable
fun MushafPageView(
    page: MushafPage,
    modifier: Modifier = Modifier,
    onAyahEvent: (AyahEvent) -> Unit
) {

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
    ) {

        val lineHeight = maxHeight / 17

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
//            verticalArrangement = Arrangement.Center
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

                    is MushafPage.ChapterLine -> MushafChaperLine(
                        line = line,
                        modifier = Modifier
                            .height(lineHeight)
                            .fillMaxWidth()
                    )

                    is MushafPage.TextLine -> MushafTextLine(
                        line = line,
                        modifier = Modifier
                            .height(lineHeight)
                            .fillMaxWidth()
                    )
                }
            }

            if (page.page <= 2) Spacer(modifier = Modifier.weight(1f))

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
fun MushafChaperLine(
    line: MushafPage.ChapterLine,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .paint(
                painterResource(id = R.drawable.ic_surah_border),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
    ) {

        Text(
            text = "${line.sura.toString().padStart(3, '0')} surah",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.surahNames,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
fun MushafTextLine(
    line: MushafPage.TextLine,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalQuranTextStyle.current,
    colorScheme: ColorScheme = MaterialTheme.colorScheme,
) {

    val text = remember(line) {
        buildAnnotatedString {
            line.words.forEachIndexed { index, word ->
                val background =
                    if (word.playing) colorScheme.surfaceColorAtElevation(2.dp) else colorScheme.surface

                withStyle(SpanStyle(background = background)) {
                    if (index > 0) append(" ")
                    when (word.charType) {
                        CharType.Word -> append(word.text)
                        CharType.End -> withStyle(
                            SpanStyle(color = colorScheme.onPrimaryContainer)
                        ) {
                            append(word.text)
                        }
                    }
                }
            }
        }
    }

    var layoutResult: TextLayoutResult? by remember { mutableStateOf(null) }

    Text(
        text = text,
        style = style.copy(textAlign = TextAlign.Justify),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentWidth(),
    )
}
