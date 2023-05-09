package org.alquran.ui.screen.audioSheet

import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.alquran.R
import org.alquran.audio.models.NowPlaying
import org.alquran.audio.models.progress
import org.alquran.ui.screen.pager.QuranEvent

@Composable
internal fun AudioBottomBar(
  modifier: Modifier = Modifier,
  uiState: NowPlaying,
  onEvent: (QuranEvent) -> Unit,
  onExpand: () -> Unit,
) {
//  LaunchedEffect(key1 = uiState) {
//    Timber.e(uiState.toString())
//  }

  Column(
    modifier = modifier
      .background(MaterialTheme.colorScheme.background)
      .clickable(onClick = onExpand)
      .fillMaxWidth()
      .navigationBarsPadding()
  ) {
    Row(
      modifier.height(AudioBottomSheetCollapsedHeight),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      AsyncImage(
        model = uiState.artWork,
        contentDescription = uiState.title,
        contentScale = ContentScale.Crop,
        placeholder = painterResource(id = R.drawable.ic_reciter),
        error = painterResource(id = R.drawable.ic_reciter),
        modifier = Modifier
          .size(AudioBottomSheetCollapsedHeight)
          .padding(12.dp)
          .aspectRatio(1f)
          .clip(MaterialTheme.shapes.medium)
          .background(MaterialTheme.colorScheme.primaryContainer)
      )

      Column(
        modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center
      ) {
        Text(
          text = uiState.title,
          style = MaterialTheme.typography.titleSmall
        )
        Text(
          text = uiState.reciterName,
          style = MaterialTheme.typography.bodyMedium
        )
      }

      IconButton(onClick = { onEvent(QuranEvent.SkipToPrevious) }) {
        Icon(painterResource(id = R.drawable.ic_skip_previous), null)
      }

      PlayPauseButton(isPlaying = uiState.isPlaying) {
        onEvent(QuranEvent.PlayOrPause)
      }

      IconButton(onClick = { onEvent(QuranEvent.SkipToNext) }) {
        Icon(painterResource(id = R.drawable.ic_skip_next), null)
      }
    }

    val progressModifier = Modifier
      .height(1.5.dp)
      .fillMaxWidth()

    if (!uiState.isPlaying && uiState.isLoading) {
      LinearProgressIndicator(
        modifier = progressModifier,
        trackColor = Color.Transparent,
      )
    } else {
      LinearProgressIndicator(
        modifier = progressModifier,
        progress = uiState.progress,
        trackColor = Color.Transparent,
      )
    }
  }
}

@Composable
internal fun PlayPauseButton(
  modifier: Modifier = Modifier,
  isPlaying: Boolean,
  onClick: () -> Unit,
) {

  IconButton(modifier = modifier, onClick = onClick) {
    val icon = AnimatedImageVector.animatedVectorResource(R.drawable.ic_play_pause)
    Icon(
      painter = rememberAnimatedVectorPainter(icon, isPlaying),
      contentDescription = null,
      modifier = Modifier.size(48.dp)
    )
  }
}

val AudioBottomSheetCollapsedHeight = 70.dp