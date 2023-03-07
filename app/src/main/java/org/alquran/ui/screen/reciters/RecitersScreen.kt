package org.muslimapp.feature.audio.ui.reciters

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import arg.quran.models.audio.Qari
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import org.alquran.R
import org.alquran.ui.components.MuslimsTopAppBarDefaults
import org.alquran.ui.components.SearchButton
import org.alquran.ui.navigation.directionToReciterRecitation
import org.alquran.ui.theme.QuranTheme
import org.alquran.utils.QariListPreviewParameterProvider
import org.alquran.utils.QariPreviewParameterProvider
import org.alquran.utils.extensions.add

@Composable
internal fun RecitersScreen(
  uiState: List<Qari>,
  navigate: (String) -> Unit,
) {
  val scrollBehavior =
    TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state = rememberTopAppBarState())

  val topBarColor by rememberUpdatedState(MuslimsTopAppBarDefaults.largeTopAppBarColors())

  Column(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)) {
    LargeTopAppBar(
      scrollBehavior = scrollBehavior,
      title = { Text(text = stringResource(id = R.string.recitations)) },
      actions = { SearchButton {} },
      colors = topBarColor,
      windowInsets = WindowInsets.statusBars,
    )

    ReciterList(uiState) {
      navigate(directionToReciterRecitation(it.slug))
    }
  }
}

@Composable
private fun ReciterList(
  uiState: List<Qari>,
  onItemClick: (Qari) -> Unit,
) {
  LazyVerticalGrid(
    contentPadding = WindowInsets.navigationBars.asPaddingValues().add(16.dp, 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    columns = GridCells.Adaptive(150.dp),
  ) {
    items(uiState) { qari ->
      ReciterItem(
        reciter = qari,
        modifier = Modifier.clickable { onItemClick(qari) },
      )
    }
  }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ReciterListScreenPreview(
  @PreviewParameter(
    QariListPreviewParameterProvider::class,
    limit = 1,
  ) reciters: List<Qari>,
) {
  ReciterList(uiState = reciters) {}
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ReciterItem(reciter: Qari, modifier: Modifier = Modifier) {

  Card {
    Column(
      modifier = Modifier
        .clip(MaterialTheme.shapes.medium)
        .then(modifier.fillMaxWidth()),
    ) {

      val colorScheme = MaterialTheme.colorScheme

      val qariName = stringResource(id = reciter.nameId)
      GlideImage(
        model = reciter.image,
        contentDescription = qariName,
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(1f)
          .background(colorScheme.onSurfaceVariant),
      ) {
        it.placeholder(R.drawable.ic_reciter).error(R.drawable.ic_reciter)
          .signature { reciter.image }
      }

      Column(
        modifier = Modifier
          .padding(horizontal = 8.dp)
          .height(90.dp),
        verticalArrangement = Arrangement.Center,
      ) {

        Text(
          text = qariName,
          style = MaterialTheme.typography.bodyLarge,
          fontWeight = FontWeight.Bold,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ReciterItemPreview(
  @PreviewParameter(QariPreviewParameterProvider::class, limit = 3) reciter: Qari,
) {
  QuranTheme {
    ReciterItem(reciter = reciter)
  }
}