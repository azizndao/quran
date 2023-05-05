package org.alquran.ui.screen.home.surahs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import arg.quran.models.SuraWithTranslation
import arg.quran.models.firstPage
import arg.quran.models.quran.VerseKey
import kotlinx.coroutines.flow.StateFlow
import org.alquran.R
import org.alquran.ui.components.LineSeparator
import org.alquran.ui.uistate.SurahListUiState
import org.alquran.views.CircularProgressLoader
import org.alquran.views.JuzHeader
import org.alquran.views.SectionTitle
import org.quran.ui.theme.QuranFontFamilies

@Composable
fun SurahList(
  modifier: Modifier = Modifier,
  uiStateFlow: StateFlow<SurahListUiState>,
  onNavigate: (Int, VerseKey?) -> Unit,
) {

  val uiState by uiStateFlow.collectAsStateWithLifecycle()

  CircularProgressLoader(uiState.loading, modifier = modifier) {
    SurahList(
      uiState = uiState,
      onNavigate = onNavigate
    )
  }
}

@Composable
private fun SurahList(
  modifier: Modifier = Modifier,
  state: LazyListState = rememberLazyListState(),
  uiState: SurahListUiState,
  onNavigate: (Int, VerseKey?) -> Unit,
) {
  val topShape = MaterialTheme.shapes.large.copy(
    bottomEnd = CornerSize(0),
    bottomStart = CornerSize(0)
  )
  val bottomShape = MaterialTheme.shapes.large.copy(
    topEnd = CornerSize(0),
    topStart = CornerSize(0)
  )
  LazyColumn(
    modifier = modifier.testTag("quran:surahList"),
    contentPadding = WindowInsets.navigationBars
      .add(WindowInsets(top = 16.dp, bottom = 16.dp, right = 12.dp, left = 12.dp))
      .asPaddingValues(),
    state = state,
//    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {

    uiState.recentSurah?.let { recentSura ->
      item(key = "recent") {
        Surface(shape = MaterialTheme.shapes.large) {
          Column {
            SectionTitle { Text(stringResource(R.string.recent)) }
            SurahItem(surah = recentSura,
              modifier = Modifier.clickable {
                onNavigate(recentSura.firstPage, VerseKey(recentSura.number, 1))
              }
            )
          }
        }
      }
    }

    for (juz in uiState.juzs) {
      item(key = juz.juz, contentType = "header") {
        JuzHeader(
          juz.juz,
          juz.page,
          modifier = Modifier
            .clip(if (juz.surahs.isEmpty()) MaterialTheme.shapes.large else topShape)
            .clickable { onNavigate(juz.page, null) }
            .background(MaterialTheme.colorScheme.surface),
        )
      }

      itemsIndexed(
        juz.surahs,
        key = { _, s -> s.nameSimple },
        contentType = { _, _ -> "surah" },
      ) { index, surah ->
        if (index != 0) LineSeparator(
          modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(start = 70.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        val shape = if (index < juz.surahs.size - 1) RectangleShape else bottomShape
        SurahItem(
          modifier = Modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onNavigate(surah.firstPage, VerseKey(surah.number, 1)) },
          surah = surah,
        )
      }
      item {
        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}

//@Preview(showBackground = true)
//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun ListSurahPreview(
//    @PreviewParameter(
//        SurahListPreviewParameterProvider::class, limit = 1
//    ) surahs: List<SurahMapping>,
//) {
//
//    val uiState = SurahListUiState(loading = false, juzs = surahs)
//
//    QuranTheme {
//        CompositionLocalProvider(LocalInsetsPadding provides WindowInsets(0.dp)) {
//            SurahList(uiState = uiState, onNavigate = { _, _ -> })
//        }
//    }
//}

@Composable
fun SurahItem(
  modifier: Modifier = Modifier,
  surah: SuraWithTranslation,
) {

  Row(
    modifier = modifier
      .fillMaxWidth()
      .heightIn(min = 80.dp)
  ) {

    Box(
      modifier = Modifier
        .padding(top = 8.dp, bottom = 8.dp, start = 16.dp)
        .widthIn(42.dp, 56.dp)
    ) {
      Text(text = "${surah.number}.", fontWeight = FontWeight.SemiBold)
    }

    Column(
      modifier = Modifier
        .padding(horizontal = 8.dp, vertical = 8.dp)
        .weight(1f)
    ) {
      Text(
        text = stringResource(
          R.string.revelation,
          surah.revelationOrder,
          stringResource(id = if (surah.isMakki) org.quram.common.R.string.makki else org.quram.common.R.string.madani),
          surah.ayahCount
        ),
        style = MaterialTheme.typography.labelSmall,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
      )

      Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column {
          Text(
            text = surah.nameSimple,
            style = MaterialTheme.typography.titleLarge
          )
          Text(
            text = surah.translation,
            style = MaterialTheme.typography.bodyMedium
          )
        }

        Spacer(modifier = Modifier.padding(16.dp))

        Text(
          text = surah.number.toString().padStart(3, '0'),
          style = MaterialTheme.typography.headlineLarge.copy(
            fontFamily = QuranFontFamilies.SuraNames,
            textDirection = TextDirection.Rtl,
            fontWeight = FontWeight.Normal
          ),
          modifier = Modifier
            .align(Alignment.Top)
            .padding(bottom = 8.dp, end = 16.dp)
        )
      }
    }
  }
}


//@Preview(showBackground = true)
//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//fun ChapterItemPreview(
//    @PreviewParameter(
//        SurahPreviewParameterProvider::class, limit = 1
//    ) surah: SurahWithTranslation,
//) {
//    QuranTheme {
//        SurahItem(surah = surah)
//    }
//}
//
