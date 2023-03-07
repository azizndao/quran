package org.alquran.ui.screen.audioSheet

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import arg.quran.models.audio.Qari
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.GlideLazyListPreloader
import kotlinx.coroutines.flow.StateFlow
import org.alquran.R
import org.alquran.audio.models.AudioState
import org.alquran.audio.models.NowPlaying
import org.alquran.audio.models.progress
import org.alquran.ui.components.BottomSheetDragHandler
import org.alquran.ui.components.LineSeparator
import org.alquran.utils.lerp
import kotlin.math.roundToLong


@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun ExpandedAudioSheet(
  modifier: Modifier = Modifier,
  contentPadding: PaddingValues,
  uiState: AudioUiState,
  playlistFlow: StateFlow<PlaylistUiState>,
  onCollapse: () -> Unit,
  onUiEvent: (AudioEvent) -> Unit,
  showReciter: (Qari) -> Unit,
  onPositionChange: (Long) -> Unit,
) {
  val colorScheme = MaterialTheme.colorScheme
  val sheetPeekHeight =
    48.dp + contentPadding.calculateBottomPadding() + contentPadding.calculateTopPadding()

  val scaffoldState = rememberBottomSheetScaffoldState()

  val fraction by remember {
    derivedStateOf {
      val progress = scaffoldState.bottomSheetState.progress
      if (scaffoldState.bottomSheetState.currentValue == BottomSheetValue.Expanded) {
        progress
      } else {
        1 - progress
      }
    }
  }

  val playlist by playlistFlow.collectAsStateWithLifecycle()

  BoxWithConstraints(modifier = modifier) {
    val height = maxHeight
    BottomSheetScaffold(
      scaffoldState = scaffoldState,
      sheetElevation = 0.dp,
      sheetBackgroundColor = colorScheme.surfaceVariant,
      sheetContentColor = colorScheme.onSurfaceVariant,
      backgroundColor = colorScheme.background,
      contentColor = colorScheme.onBackground,
      sheetContent = {
        Column(modifier = Modifier.height(height)) {
          BottomSheetDragHandler()

          val state = rememberLazyListState(playlist.playingIndex)

          LaunchedEffect(key1 = playlist) {
            state.animateScrollToItem(playlist.playingIndex)
          }

          val expandedAlpha by remember {
            derivedStateOf { lerp(0f, 1f, 0.2f, 0.5f, fraction) }
          }

          LineSeparator(
            Modifier.graphicsLayer { alpha = expandedAlpha },
            color = colorScheme.onSurfaceVariant.copy(alpha = 0.15f)
          )

          LazyColumn(
            modifier = Modifier.graphicsLayer { alpha = expandedAlpha },
            contentPadding = WindowInsets.navigationBars.add(WindowInsets(bottom = contentPadding.calculateTopPadding()))
              .asPaddingValues(),
            state = state
          ) {

            items(playlist.items) { item ->
              SurahRecitationItem(
                uiState = item,
                onClick = { onUiEvent(AudioEvent.Play(item.reciterId, item.sura)) })
            }
          }
        }
      },
      sheetPeekHeight = sheetPeekHeight,
      sheetShape = MaterialTheme.shapes.extraLarge.copy(
        bottomStart = CornerSize(0),
        bottomEnd = CornerSize(0),
      )
    ) {
      ExpandedContent(
        onCollapse = onCollapse,
        uiState = uiState,
        onUiEvent = onUiEvent,
        showReciter = showReciter,
        onPositionChange = onPositionChange,
        modifier = Modifier
          .height(this@BoxWithConstraints.maxHeight - contentPadding.calculateTopPadding())
      )
    }

  }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun SurahRecitationItem(
  modifier: Modifier = Modifier,
  uiState: PlaylistUiState.Item,
  onClick: () -> Unit,
) {

  val backgroundColor by animateColorAsState(
    if (uiState.isPlaying) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
  )

  Surface(
    modifier = Modifier.padding(horizontal = 8.dp),
    color = backgroundColor,
    shape = MaterialTheme.shapes.medium,
    onClick = onClick
  ) {
    Row(
      modifier = modifier.padding(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(52.dp)
          .clip(MaterialTheme.shapes.medium)
          .background(MaterialTheme.colorScheme.secondaryContainer),
      ) {

        GlideImage(
          model = uiState.artWork,
          contentDescription = uiState.reciterName,
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        ) { it.placeholder(R.drawable.ic_reciter).error(R.drawable.ic_reciter) }

        Text(
          text = uiState.sura.toString(),
          fontWeight = FontWeight.Bold,
          modifier = Modifier.align(Alignment.Center),
        )
      }
      Column(
        modifier = Modifier
          .weight(1f)
          .padding(horizontal = 16.dp),
      ) {
        Text(
          text = uiState.suraName,
          style = MaterialTheme.typography.bodyLarge,
          fontWeight = FontWeight.Bold,
        )
        Text(
          text = uiState.reciterName,
          style = MaterialTheme.typography.bodySmall,
        )
      }
    }
  }
}

@Composable
internal fun ExpandedContent(
  modifier: Modifier = Modifier,
  onCollapse: () -> Unit,
  uiState: AudioUiState,
  onUiEvent: (AudioEvent) -> Unit,
  showReciter: (Qari) -> Unit,
  onPositionChange: (Long) -> Unit
) {
  Column(
    verticalArrangement = Arrangement.SpaceBetween,
    modifier = modifier
      .background(MaterialTheme.colorScheme.surface)
      .padding(bottom = 24.dp)
      .fillMaxSize()
      .navigationBarsPadding()
      .padding(bottom = 45.dp),
  ) {

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = onCollapse) {
        Icon(
          painterResource(id = R.drawable.ic_arrow_down),
          stringResource(id = R.string.more)
        )
      }
      Text(
        text = stringResource(id = R.string.now_playing),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(start = 16.dp)
      )
      IconButton(onClick = { }) {
        Icon(
          painterResource(id = R.drawable.ic_more_vert),
          stringResource(id = R.string.more)
        )
      }
    }

    ReciterList(
      uiState,
      onReciterChange = { reciter -> onUiEvent(AudioEvent.ChangeReciter(reciter)) },
      showReciter = showReciter
    )

    Column(modifier = Modifier.fillMaxWidth()) {
      Text(
        text = uiState.playing?.title ?: "",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
          .fillMaxWidth()
          .wrapContentWidth()
      )

      Text(
        text = uiState.playing?.reciterName ?: "",
        modifier = Modifier
          .fillMaxWidth()
          .wrapContentWidth()
      )

      CustomSlider(playing = uiState.playing, onPositionChange = onPositionChange)

      Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 18.dp)
      ) {
        Text(
          text = uiState.playing?.positionStr.toString(),
          style = TextStyle(
            fontSize = 12.sp,
            color = LocalContentColor.current.copy(alpha = 0.7f)
          )
        )
        Text(
          text = uiState.playing?.durationStr.toString(),
          style = TextStyle(
            fontSize = 12.sp,
            color = LocalContentColor.current.copy(alpha = 0.7f)
          )
        )
      }
    }

    PlayerControlBar(
      uiState = uiState,
      onUiEvent = onUiEvent,
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
    mutableStateOf(playing?.progress ?: 0f)
  }

  org.alquran.ui.components.CustomSlider(
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ReciterList(
  uiState: AudioUiState,
  onReciterChange: (Qari) -> Unit,
  showReciter: (Qari) -> Unit,
) {
  val index = remember { uiState.qaris.indexOfFirst { uiState.currentReciterId == it.slug } }

  val state = rememberLazyListState(
    if (index == -1) 0 else index,
    -with(LocalDensity.current) { 42.dp.roundToPx() }
  )

  BoxWithConstraints {
    LazyRow(
      contentPadding = PaddingValues(horizontal = 16.dp),
      state = state,
      horizontalArrangement = Arrangement.spacedBy(16.dp),
      flingBehavior = rememberSnapFlingBehavior(state)
    ) {
      items(uiState.qaris) { qari ->
        ReciterItem(
          modifier = Modifier.width(maxWidth - 72.dp),
          qari = qari,
          selected = uiState.currentReciterId == qari.slug,
          onClick = { onReciterChange(qari) }
        ) { showReciter(qari) }
      }
    }

    GlideLazyListPreloader(
      state = state,
      data = uiState.qaris,
      size = Size(50f, 50f),
      numberOfItemsToPreload = 15,
      fixedVisibleItemCount = 2,
    ) { item, requestBuilder ->
      requestBuilder.load(item.image)
    }
  }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ReciterItem(
  modifier: Modifier = Modifier,
  qari: Qari,
  selected: Boolean = false,
  onClick: () -> Unit,
  onOptionClick: () -> Unit,
) {
  val colorScheme = MaterialTheme.colorScheme
  Column(modifier = modifier) {
    Box {

      val shapes = MaterialTheme.shapes

      val imageModifier = remember(selected) {
        var m = Modifier
          .aspectRatio(1f)
          .clip(shapes.extraLarge)

        if (selected) m = m.border(4.dp, color = colorScheme.primary, shapes.extraLarge)

        m
          .background(colorScheme.surfaceVariant)
          .selectable(selected, onClick = onClick)
      }

      GlideImage(
        model = qari.image,
        contentDescription = stringResource(id = qari.nameId),
        contentScale = ContentScale.Crop,
        modifier = imageModifier,
      ) {
        it.placeholder(R.drawable.ic_reciter).error(R.drawable.ic_reciter)
      }
      IconButton(
        onClick = onOptionClick,
        modifier = Modifier.align(Alignment.TopEnd)
      ) {
        Icon(
          painterResource(id = R.drawable.ic_download),
          null,
          tint = colorScheme.onSurfaceVariant
        )
      }
    }

    Text(
      stringResource(id = qari.nameId),
      modifier = Modifier
        .align(Alignment.CenterHorizontally)
        .padding(8.dp),
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      style = MaterialTheme.typography.headlineMedium
    )
  }
}


@OptIn(ExperimentalAnimationGraphicsApi::class)
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

    IconButton(onClick = { onUiEvent(AudioEvent.SkipPrevious) }) {
      Icon(
        painter = painterResource(id = R.drawable.ic_repeat_one),
        contentDescription = null,
      )
    }

    IconButton(onClick = { onUiEvent(AudioEvent.SkipPrevious) }) {
      Icon(
        painter = painterResource(id = R.drawable.ic_skip_previous),
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

      val icon = AnimatedImageVector.animatedVectorResource(R.drawable.ic_play_pause)
      Icon(
        painter = rememberAnimatedVectorPainter(
          icon,
          uiState.audioState == AudioState.PLAYING
        ),
        contentDescription = null,
        modifier = Modifier.scale(1.25f)
      )
    }

    IconButton(onClick = { onUiEvent(AudioEvent.SkipNext) }) {
      Icon(
        painter = painterResource(id = R.drawable.ic_skip_next),
        contentDescription = null,
        modifier = Modifier.scale(1.25f)
      )
    }

    IconButton(onClick = { onUiEvent(AudioEvent.SkipNext) }) {
      Icon(
        painter = painterResource(id = R.drawable.ic_shuffle),
        contentDescription = null,
      )
    }
  }
}
