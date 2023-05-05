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
import org.alquran.audio.models.AudioState
import org.alquran.audio.models.progress

@Composable
internal fun AudioBottomBar(
  modifier: Modifier = Modifier,
  uiState: AudioUiState,
  onExpand: () -> Unit,
) {
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
        model = uiState.playing?.artWork,
        contentDescription = uiState.playing?.title,
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
          text = uiState.playing?.title ?: "",
          style = MaterialTheme.typography.titleSmall
        )
        Text(
          text = uiState.playing?.reciterName ?: "",
          style = MaterialTheme.typography.bodyMedium
        )
      }

      IconButton(onClick = { uiState.onAudioEvent(AudioEvent.SkipPrevious) }) {
        Icon(painterResource(id = R.drawable.ic_skip_previous), null)
      }

      PlayPauseButton(isPlaying = uiState.audioState == AudioState.PLAYING) {
        uiState.onAudioEvent(AudioEvent.PlayOrPause)
      }

      IconButton(onClick = { uiState.onAudioEvent(AudioEvent.SkipNext) }) {
        Icon(painterResource(id = R.drawable.ic_skip_next), null)
      }
    }

    val progressModifier = Modifier
      .height(1.5.dp)
      .fillMaxWidth()


    if (uiState.audioState == AudioState.LOADING) {
      LinearProgressIndicator(
        modifier = progressModifier,
        trackColor = Color.Transparent,
      )
    } else {
      LinearProgressIndicator(
        modifier = progressModifier,
        progress = uiState.playing?.progress ?: 0f,
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