package org.quran.features.audio

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
import com.bumptech.glide.integration.compose.GlideImage
import org.quran.core.audio.models.NowPlaying
import org.quran.core.audio.models.progress


@Composable
fun AudioBottomBar(
  modifier: Modifier = Modifier,
  uiState: NowPlaying,
  onEvent: (AudioEvent) -> Unit,
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
      GlideImage(
        model = uiState.artWork,
        contentDescription = uiState.title,
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .size(AudioBottomSheetCollapsedHeight)
          .padding(12.dp)
          .aspectRatio(1f)
          .clip(MaterialTheme.shapes.medium)
          .background(MaterialTheme.colorScheme.primaryContainer)
      ) {
        it.placeholder(org.quran.ui.R.drawable.ic_reciter)
          .error(org.quran.ui.R.drawable.ic_reciter)
      }

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

      IconButton(onClick = { onEvent(AudioEvent.SkipToPrevious) }) {
        Icon(painterResource(id = org.quran.ui.R.drawable.ic_skip_previous), null)
      }

      PlayPauseButton(isPlaying = uiState.isPlaying) {
        onEvent(AudioEvent.PlayOrPause)
      }

      IconButton(onClick = { onEvent(AudioEvent.SkipToNext) }) {
        Icon(painterResource(id = org.quran.ui.R.drawable.ic_skip_next), null)
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
    val icon = AnimatedImageVector.animatedVectorResource(org.quran.ui.R.drawable.ic_play_pause)
    Icon(
      painter = rememberAnimatedVectorPainter(icon, isPlaying),
      contentDescription = null,
      modifier = Modifier.size(48.dp)
    )
  }
}

val AudioBottomSheetCollapsedHeight = 70.dp