package org.quran.features.home.juzs

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import arg.quran.models.HizbQuarter
import kotlinx.coroutines.flow.StateFlow
import org.alquran.ui.components.LineSeparator
import org.quran.ui.components.CircularProgressLoader
import org.quran.ui.R as Ui

@Composable
fun ListJuzs(
  modifier: Modifier = Modifier,
  uiStateFlow: StateFlow<JuzListUiState>,
  onNavigate: (HizbQuarter) -> Unit,
) {
  val uiState by uiStateFlow.collectAsStateWithLifecycle()
  CircularProgressLoader(uiState.loading) {
    ListJuz(
      modifier = modifier,
      uiState = uiState,
      onItemClick = onNavigate
    )
  }
}

@Composable
private fun ListJuz(
  modifier: Modifier = Modifier,
  listState: LazyListState = rememberLazyListState(),
  uiState: JuzListUiState,
  onItemClick: (HizbQuarter) -> Unit,
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
    modifier = modifier,
    state = listState,
    contentPadding = WindowInsets.navigationBars
      .add(WindowInsets(bottom = 16.dp, left = 12.dp, right = 12.dp, top = 16.dp))
      .asPaddingValues(),
  ) {
    for ((juz, page, quarters) in uiState.data) {
      item {
        JuzHeader(
          juz,
          page,
          modifier = Modifier
            .clip(topShape)
            .clickable { onItemClick(quarters.first()) }
            .background(MaterialTheme.colorScheme.surface),
        )
      }

      itemsIndexed(
        quarters,
        key = { _, it -> it.hizbQuarter },
        contentType = { _, _ -> "hibz" }) { index, quarter ->

        if (index != 0) LineSeparator(
          modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(start = 60.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        val shape = if (index < quarters.size - 1) RectangleShape else bottomShape
        HizbQuarterItem(
          index = index,
          item = quarter,
          modifier = Modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onItemClick(quarter) }
        )
      }
      item {
        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}


@Composable
private fun HizbQuarterItem(
  modifier: Modifier = Modifier,
  index: Int,
  item: HizbQuarter,
) {
  val colorScheme = MaterialTheme.colorScheme
  Row(
    modifier = modifier.padding(horizontal = 16.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      modifier = Modifier
        .padding(vertical = 8.dp)
        .size(42.dp)
        .aspectRatio(1f)
        .clip(CircleShape)
        .background(colorScheme.secondaryContainer)
    ) {
      Canvas(modifier = Modifier.fillMaxSize()) {
        translate(top = -size.height * 0.25f, left = -size.width * 0.25f) {
          val size1 = Size(size.width * 1.5f, size.height * 1.5f)
          if (index % 4 != 0) {
            drawArc(
              color = colorScheme.secondary,
              startAngle = -90f,
              sweepAngle = 90f * if (index < 4) index else index - 4,
              useCenter = true,
              size = size1
            )
          } else {
            drawOval(color = colorScheme.secondaryContainer, size = size1)
          }
        }
      }
      if (index % 4 == 0) {
        Text(
          text = (((item.hizbQuarter - 1) / 4) + 1).toString(),
          color = colorScheme.onSecondaryContainer,
          modifier = Modifier.align(Alignment.Center),
          fontWeight = FontWeight.Bold,
        )
      }
    }
    Text(
      stringResource(
        id = Ui.string.verse_location,
        item.surahName,
        item.surahNumber,
        item.ayahNumberInSurah,
      ),
      modifier = Modifier
        .weight(1f)
        .padding(horizontal = 16.dp),
    )

    Text(
      item.page.toString(),
      color = LocalContentColor.current.copy(alpha = 0.45f)
    )
  }
}

