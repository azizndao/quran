package org.quran.features.pager.components

import android.content.res.Configuration
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.quran.features.pager.R
import org.quran.features.pager.uiState.TranslationPage
import org.quran.ui.theme.QuranTheme


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SurahHeader(
  modifier: Modifier = Modifier,
  suraNumber: Int,
  surahName: String,
) {

  Column(modifier = modifier) {

    SurahHeader(name = surahName)

    if (suraNumber != 1 && suraNumber != 9) {
      Image(
        painter = painterResource(id = R.drawable.bismillah),
        contentDescription = null,
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .padding(top = 8.dp),
        contentScale = ContentScale.Inside,
        colorFilter = ColorFilter.tint(LocalContentColor.current)
      )
    }
  }
}

@ExperimentalAnimationApi
@Composable
private fun SurahHeader(modifier: Modifier = Modifier, name: String) {

  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 12.dp)
      .paint(
        painterResource(id = R.drawable.surah_border),
        contentScale = ContentScale.Fit,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
      )
  ) {

    Text(
      text = stringResource(id = org.quran.ui.R.string.surah, name),
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.titleSmall,
      modifier = Modifier.align(Alignment.Center)
    )
  }
}

@ExperimentalAnimationApi
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NewSurahItemPreview() {
  val sura = remember {
    TranslationPage.Chapter(number = 2, name = "Al-Baqarah")
  }
  QuranTheme {
    Surface {
      SurahHeader(surahName = sura.name, suraNumber = sura.number)
    }
  }
}

