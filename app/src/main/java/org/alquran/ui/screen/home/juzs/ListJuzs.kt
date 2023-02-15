package org.alquran.ui.screen.home.juzs

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow
import org.alquran.R
import org.alquran.ui.components.LineSeparator
import org.muslimapp.feature.quran.ui.LocalInsetsPadding
import org.muslimapp.feature.quran.views.CircularProgressLoader
import org.muslimapp.feature.quran.views.JuzHeader
import org.quram.common.model.HizbQuarter

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

    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = LocalInsetsPadding.current
            .add(WindowInsets(bottom = 16.dp, left = 12.dp, right = 12.dp, top = 16.dp))
            .asPaddingValues(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        items(
            uiState.data,
            key = { it.number },
            contentType = { "hibz" }) { (juz, page, quarters) ->

            Surface(shape = MaterialTheme.shapes.large) {
                Column {
                    JuzHeader(
                        juz,
                        page,
                        modifier = Modifier.clickable { onItemClick(quarters.first()) })

                    quarters.forEachIndexed { index, quarter ->
                        if (index > 0) LineSeparator(startIndent = 80.dp)
                        HizbQuarterItem(
                            index = index,
                            item = quarter,
                            modifier = Modifier.clickable { onItemClick(quarter) })
                    }
                }
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
                id = R.string.verse_location,
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

