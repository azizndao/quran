package org.quran.features.audio

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import arg.quran.models.audio.Qari
import com.bumptech.glide.integration.compose.GlideImage
import org.quran.core.audio.models.NowPlaying
import org.quran.core.audio.models.progress
import kotlin.math.roundToLong

@Composable
fun AudioMenuSheet(
  audioUiState: AudioUiState,
  onEvent: (AudioEvent) -> Unit,
  onDismissRequest: () -> Unit,
  showReciter: (Qari) -> Unit,
) {
  ModalBottomSheet(
    onDismissRequest = onDismissRequest,
    windowInsets = WindowInsets(0, 0, 0, 0),
    sheetState = rememberModalBottomSheetState(true)
  ) {

    ReciterList(
      qaris = audioUiState.qaris,
      uiState = audioUiState,
      onReciterChange = { reciter -> onEvent(AudioEvent.ChangeReciter(reciter)) },
      showReciter = showReciter
    )

    Column(modifier = Modifier.fillMaxWidth()) {
      Text(
        text = audioUiState.playing?.title ?: "",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
          .fillMaxWidth()
          .wrapContentWidth()
      )

      Text(
        text = audioUiState.playing?.reciterName ?: "",
        modifier = Modifier
          .fillMaxWidth()
          .wrapContentWidth()
      )

      CustomSlider(
        playing = audioUiState.playing,
        onPositionChange = { onEvent(AudioEvent.PositionChanged(it)) })

      Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 18.dp)
      ) {
        Text(
          text = audioUiState.playing?.positionStr.toString(),
          style = TextStyle(
            fontSize = 12.sp,
            color = LocalContentColor.current.copy(alpha = 0.7f)
          )
        )
        Text(
          text = audioUiState.playing?.durationStr.toString(),
          style = TextStyle(
            fontSize = 12.sp,
            color = LocalContentColor.current.copy(alpha = 0.7f)
          )
        )
      }
    }

    PlayerControlBar(
      uiState = audioUiState,
      onUiEvent = onEvent,
    )

    Box(
      modifier = Modifier
        .navigationBarsPadding()
        .padding(bottom = 24.dp)
    )
  }
}

@Composable
private fun CustomSlider(
  modifier: Modifier = Modifier,
  playing: NowPlaying?,
  onPositionChange: (Long) -> Unit
) {

  val (position, setPosition) = remember(playing?.progress) {
    mutableFloatStateOf(playing?.progress ?: 0f)
  }

  org.quran.ui.components.CustomSlider(
    value = position,
    onValueChange = setPosition,
    onValueChangeFinished = {
      onPositionChange(((playing?.duration ?: 0) * position).roundToLong())
    },
    modifier = modifier
      .padding(horizontal = 16.dp)
      .fillMaxWidth(),
  )
}

@Composable
private fun ReciterList(
  qaris: List<Qari>,
  uiState: AudioUiState,
  onReciterChange: (Qari) -> Unit,
  showReciter: (Qari) -> Unit,
) {
  val index = remember { qaris.indexOfFirst { uiState.currentReciterId == it.slug } }

  val state = rememberLazyListState(
    if (index == -1) 0 else index,
    -with(LocalDensity.current) { 42.dp.roundToPx() }
  )

  BoxWithConstraints {
    LazyRow(
      contentPadding = PaddingValues(16.dp),
      state = state,
      horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      items(qaris) { reciter ->
        ReciterItem(
          reciter = reciter,
          selected = uiState.currentReciterId == reciter.slug,
          onClick = { onReciterChange(reciter) }
        ) { showReciter(reciter) }
      }
    }
  }
}

@Composable
private fun ReciterItem(
  modifier: Modifier = Modifier,
  reciter: Qari,
  selected: Boolean = false,
  onClick: () -> Unit,
  onOptionClick: () -> Unit,
) {
  val colorScheme = MaterialTheme.colorScheme
  Card(
    modifier = modifier
      .requiredWidth(138.dp),
    onClick = onClick
  ) {
    Column {
      Box {
        val color by animateColorAsState(
          if (selected) colorScheme.onPrimaryContainer else colorScheme.primaryContainer,
          label = "ReciterImageBackground"
        )
        GlideImage(
          model = reciter.imageUrl,
          contentDescription = stringResource(reciter.nameResource),
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(color),
        ) {
          it.placeholder(org.quran.ui.R.drawable.ic_reciter)
            .error(org.quran.ui.R.drawable.ic_reciter)
        }

        IconButton(
          onClick = onOptionClick, modifier = Modifier.align(Alignment.TopEnd)
        ) {
          Icon(
            painterResource(id = org.quran.ui.R.drawable.ic_download),
            null,
            tint = colorScheme.onSurfaceVariant
          )
        }
      }

      Text(
        stringResource(id = reciter.nameResource),
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(16 / 9f)
          .padding(8.dp),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.labelLarge,
        textAlign = TextAlign.Center,
      )
    }
  }
}


@Composable
private fun PlayerControlBar(
  modifier: Modifier = Modifier,
  uiState: AudioUiState,
  onUiEvent: (AudioEvent) -> Unit,
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceEvenly
  ) {

    IconButton(onClick = { onUiEvent(AudioEvent.SkipToPrevious) }) {
      Icon(
        painter = painterResource(id = org.quran.ui.R.drawable.ic_repeat_one),
        contentDescription = null,
      )
    }

    IconButton(onClick = { onUiEvent(AudioEvent.SkipToPrevious) }) {
      Icon(
        painter = painterResource(id = org.quran.ui.R.drawable.ic_skip_previous),
        contentDescription = null,
        modifier = Modifier.scale(1.25f)
      )
    }

    LargeFloatingActionButton(
      onClick = { onUiEvent(AudioEvent.PlayOrPause) },
      elevation = FloatingActionButtonDefaults.elevation(0.dp),
      contentColor = MaterialTheme.colorScheme.onPrimary,
      containerColor = MaterialTheme.colorScheme.primary,
    ) {

      val icon = AnimatedImageVector.animatedVectorResource(org.quran.ui.R.drawable.ic_play_pause)
      Icon(
        painter = rememberAnimatedVectorPainter(
          icon, uiState.audioState.isPlaying
        ), contentDescription = null, modifier = Modifier.scale(1.25f)
      )
    }

    IconButton(onClick = { onUiEvent(AudioEvent.SkipToNext) }) {
      Icon(
        painter = painterResource(id = org.quran.ui.R.drawable.ic_skip_next),
        contentDescription = null,
        modifier = Modifier.scale(1.25f)
      )
    }

    IconButton(onClick = { onUiEvent(AudioEvent.SkipToNext) }) {
      Icon(
        painter = painterResource(id = org.quran.ui.R.drawable.timer),
        contentDescription = null,
      )
    }
  }
}