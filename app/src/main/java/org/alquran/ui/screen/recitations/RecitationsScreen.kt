package org.alquran.ui.screen.recitations

import android.content.res.Configuration
import androidx.annotation.OptIn
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download
import arg.quran.models.Sura
import arg.quran.models.audio.Qari
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import org.alquran.R
import org.alquran.ui.components.BackButton
import org.alquran.ui.theme.QuranTheme
import org.alquran.utils.QariPreviewParameterProvider

@Composable
fun RecitationsScreen(
  uiState: RecitationsUiState,
  playFromSurah: (Sura) -> Unit,
  play: () -> Unit,
  download: (SurahRecitationUiState) -> Unit,
  popBackStack: () -> Unit,
) {

  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
  val listState = rememberLazyListState()

  val hostState = remember { SnackbarHostState() }
  val context = LocalContext.current

  LaunchedEffect(uiState) {
    uiState.errorMessages.firstOrNull()?.let { errorId ->
      hostState.showSnackbar(
        context.getString(errorId),
        context.getString(R.string.dismiss),
        withDismissAction = true
      )
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { ReciterBigCard(qari = uiState.reciter) },
        navigationIcon = { BackButton(onClick = popBackStack) },
        scrollBehavior = scrollBehavior
      )
    },
    snackbarHost = { SnackbarHost(hostState = hostState) }
  ) { innerPadding ->

    val contentPadding = WindowInsets.navigationBars.add(
      WindowInsets(
        top = innerPadding.calculateTopPadding(),
        bottom = innerPadding.calculateBottomPadding(),
        left = innerPadding.calculateLeftPadding(LocalLayoutDirection.current),
        right = innerPadding.calculateRightPadding(LocalLayoutDirection.current)
      )
    ).asPaddingValues()


    Surface {
      LazyColumn(
        state = listState,
        contentPadding = contentPadding
      ) {

        item(key = "controls") { ControlBar(onShuffle = {}, onPlayAll = play) }

        items(uiState.recitations, key = { s -> s.sura.number }) { recitation ->
          SurahRecitationItem(
            recitation = recitation,
            onClick = { playFromSurah(recitation.sura) },
            onOptionClick = {},
            onDownload = { download(recitation) }
          )
        }
      }
    }
  }
}

@Composable
private fun ControlBar(onShuffle: () -> Unit, onPlayAll: () -> Unit) {
  Row(
    modifier = Modifier
      .padding(horizontal = 16.dp)
      .height(56.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {

    OutlinedButton(onClick = onShuffle, modifier = Modifier.weight(1f)) {
      Icon(
        painter = painterResource(id = R.drawable.ic_shuffle),
        contentDescription = null,
        modifier = Modifier.size(ButtonDefaults.IconSize),
      )
      Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
      Text(text = stringResource(id = R.string.suffle))
    }

    Spacer(modifier = Modifier.width(16.dp))

    Button(onClick = onPlayAll, modifier = Modifier.weight(1f)) {
      Icon(
        painterResource(R.drawable.ic_play_arrow), null,
        modifier = Modifier.size(ButtonDefaults.IconSize),
      )
      Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
      Text(text = stringResource(id = R.string.play))
    }
  }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ControlBarPreview() {
  QuranTheme {
    Surface {
      ControlBar(onShuffle = { }) {}
    }
  }
}

@kotlin.OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ReciterBigCard(modifier: Modifier = Modifier, qari: Qari?) {

  Row(
    modifier = modifier
      .fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {

    val qariName = qari?.nameId?.let { stringResource(id = it) }
    GlideImage(
      model = qari?.image,
      contentScale = ContentScale.Crop,
      alignment = Alignment.TopStart,
      contentDescription = qariName,
      modifier = Modifier
        .size(48.dp)
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.secondaryContainer)
    ) { it.placeholder(R.drawable.ic_reciter).error(R.drawable.ic_reciter) }

    Text(
      text = qariName ?: "",
      style = MaterialTheme.typography.titleLarge,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
    )
  }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ReciterBigCardPreview(
  @PreviewParameter(QariPreviewParameterProvider::class, limit = 1) qari: Qari,
) {
  QuranTheme {
    Surface {
      ReciterBigCard(qari = qari)
    }
  }
}

@Composable
private fun SurahRecitationItem(
  modifier: Modifier = Modifier,
  recitation: SurahRecitationUiState,
  onClick: () -> Unit,
  onOptionClick: () -> Unit,
  onDownload: () -> Unit,
) {

  val backgroundColor by animateColorAsState(
    if (recitation.isPlaying) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
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
          .background(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.shapes.medium
          ),
      ) {
        Text(
          text = recitation.sura.number.toString(),
          fontWeight = FontWeight.Bold,
          modifier = Modifier.align(Alignment.Center),
        )
      }
      Text(
        text = recitation.sura.nameSimple,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
          .weight(1f)
          .padding(start = 16.dp),
      )
      TrailingButton(
        name = recitation.sura.nameSimple,
        download = recitation.download,
        onDownload = onDownload,
        onOptionClick = onOptionClick,
      )
    }
  }
}

@OptIn(UnstableApi::class)
@Composable
fun TrailingButton(
  name: String,
  download: Download?,
  onDownload: () -> Unit,
  onOptionClick: () -> Unit
) {
  when {
    download == null || download.isTerminalState && download.percentDownloaded != 100f -> {
      IconButton(
        onClick = onDownload
      ) {
        Icon(
          painterResource(R.drawable.ic_download),
          stringResource(
            R.string.start_downloading,
            name
          )
        )
      }
    }

    download.isTerminalState && download.percentDownloaded == 100f -> IconButton(onClick = onOptionClick) {
      Icon(
        painterResource(R.drawable.ic_more_vert),
        stringResource(id = R.string.more)
      )
    }

    else -> Box {
      CircularProgressIndicator(
        progress = download.percentDownloaded / 100,
        modifier = Modifier.matchParentSize(),
      )
      IconButton(onClick = { /*TODO*/ }) {
        Icon(
          painterResource(R.drawable.ic_clear),
          stringResource(R.string.stop_downloading_surah, name)
        )
      }
    }
  }
}

//@ExperimentalMaterial3Api
//@Preview(showBackground = true)
//@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
//@Composable
//fun SurahRecitationItemPreview(
//    @PreviewParameter(SurahPreviewParameterProvider::class, limit = 1) surah: SurahWithTranslation,
//) {
//    MuslimsTheme {
//        Surface {
//            SurahRecitationItem(
//                recitation = SurahRecitationUiState(
//                    surah,
//                    isPlaying = false,
//                    downloadedAyahs = emptyList()
//                ),
//                onDownload = {},
//                onClick = {},
//                onOptionClick = {}
//            )
//        }
//    }
//}