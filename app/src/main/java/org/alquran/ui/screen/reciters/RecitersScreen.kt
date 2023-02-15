package org.muslimapp.feature.audio.ui.reciters

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
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
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import org.alquran.R
import org.alquran.audio.models.Reciter
import org.alquran.ui.components.MuslimsTopAppBarDefaults
import org.alquran.ui.components.SearchButton
import org.alquran.ui.navigation.directionToReciterRecitation
import org.alquran.ui.theme.QuranTheme
import org.alquran.utils.ReciterListPreviewParameterProvider
import org.alquran.utils.ReciterPreviewParameterProvider
import org.alquran.utils.extensions.add

@Composable
internal fun RecitersScreen(
    uiState: List<Reciter>,
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
            navigate(directionToReciterRecitation(it.id))
        }
    }
}

@Composable
private fun ReciterList(
    uiState: List<Reciter>,
    onItemClick: (Reciter) -> Unit,
) {
    LazyVerticalGrid(
        contentPadding = WindowInsets.navigationBars.asPaddingValues().add(16.dp, 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        columns = GridCells.Adaptive(150.dp),
    ) {
        items(uiState) { reciter ->
            ReciterItem(
                reciter = reciter,
                modifier = Modifier.clickable { onItemClick(reciter) },
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ReciterListScreenPreview(
    @PreviewParameter(
        ReciterListPreviewParameterProvider::class,
        limit = 1,
    ) reciters: List<Reciter>,
) {
    ReciterList(uiState = reciters) {}
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ReciterItem(reciter: Reciter, modifier: Modifier = Modifier) {

    Card {
        Column(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .then(modifier.fillMaxWidth()),
        ) {

            val colorScheme = MaterialTheme.colorScheme

            GlideImage(
                model = reciter.image,
                contentDescription = reciter.name,
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
                    text = reciter.name,
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
    @PreviewParameter(ReciterPreviewParameterProvider::class, limit = 3) reciter: Reciter,
) {
    QuranTheme {
        ReciterItem(reciter = reciter)
    }
}