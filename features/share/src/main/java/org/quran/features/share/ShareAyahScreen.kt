package org.quran.features.share

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.quran.features.share.utils.ShareAyahPreviewParameterProvider
import org.quran.features.share.views.ShareCardView
import org.quran.ui.components.CircularProgressLoader
import org.quran.ui.theme.QuranTheme
import org.quran.ui.utils.extensions.htmlToAnnotatedString
import org.quran.ui.R as UI

@Composable
internal fun ShareAyahScreen(
  share: (ShareCardView) -> Unit,
  uiState: ShareAyahUiState,
  popBackStack: () -> Unit,
) {
  QuranTheme(darkTheme = true) {
    Surface(color = MaterialTheme.colorScheme.background) {
      CircularProgressLoader(loading = uiState is ShareAyahUiState.Loading) {
        ShareAyahScreen(uiState, onBackPress = popBackStack, share)
      }
    }
  }
}

@Composable
private fun ShareAyahScreen(
  data: ShareAyahUiState,
  onBackPress: () -> Unit,
  onShare: (ShareCardView) -> Unit,
) {

  var card by remember { mutableStateOf<ShareCardView?>(null) }

  Column(modifier = Modifier.systemBarsPadding()) {
    Box(
      modifier = Modifier
        .weight(1f)
    ) {
      AndroidView(
        modifier = Modifier
          .align(Alignment.Center)
          .wrapContentSize(),
        factory = { innerContext ->
          ShareCardView(innerContext) { ShareCard(uiState = data) }.also { view ->
            view.post { card = view }
          }
        }
      )
    }

    BottomMenu()

    NavigationBar(onBackPress) { onShare(card!!) }
  }
}

@Composable
private fun BottomMenu() {
  Row(
    modifier = Modifier
      .horizontalScroll(rememberScrollState())
      .navigationBarsPadding()
      .padding(horizontal = 16.dp)
      .fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    ElevatedAssistChip(
      onClick = { /*TODO*/ },
      leadingIcon = { Icon(painterResource(id = UI.drawable.ic_palette), null) },
      label = { Text(stringResource(id = UI.string.color_palette)) }
    )
    ElevatedAssistChip(
      onClick = { /*TODO*/ },
      leadingIcon = { Icon(painterResource(id = UI.drawable.ic_edit), null) },
      label = { Text(stringResource(id = UI.string.text)) }
    )
    ElevatedAssistChip(
      onClick = { /*TODO*/ },
      leadingIcon = { Icon(painterResource(id = UI.drawable.ic_translate), null) },
      label = { Text(stringResource(id = UI.string.translation)) }
    )
    ElevatedAssistChip(
      onClick = { /*TODO*/ },
      leadingIcon = { Icon(painterResource(id = UI.drawable.ic_translate), null) },
      label = { Text(stringResource(id = UI.string.translation)) }
    )
  }
}

@Composable
private fun NavigationBar(onBackPress: () -> Unit, onShare: () -> Unit) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    TextButton(onClick = onBackPress) {
      Text(stringResource(id = org.quram.common.R.string.canceling))
    }

    FilledTonalButton(onClick = onShare) {
      Text(stringResource(id = org.quran.ui.R.string.share))
    }
  }
}

@Composable
private fun ShareCard(
  modifier: Modifier = Modifier,
  uiState: ShareAyahUiState,
) {

  if (uiState !is ShareAyahUiState.Success) return

  val colorScheme = MaterialTheme.colorScheme

  Surface(modifier = modifier) {

    Column(modifier = Modifier.padding(24.dp)) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 8.dp)
      ) {
        Icon(
          painterResource(id = UI.drawable.logo),
          contentDescription = stringResource(id = UI.string.app_name),
          tint = colorScheme.primary,
          modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
          stringResource(id = UI.string.app_name),
          fontWeight = FontWeight.Bold,
          color = colorScheme.onSurface.copy(alpha = 0.7f),
        )
      }

      val textStyle = LocalTextStyle.current

      AnimatedVisibility(visible = uiState.text != null) {
        Text(
          uiState.text!!,
          style = textStyle,
          modifier = Modifier.fillMaxWidth(),
          textAlign = TextAlign.Center
        )
      }

      val secondaryTextStyle = MaterialTheme.typography.bodyMedium

      AnimatedVisibility(visible = uiState.translation != null) {
        Text(
          text = htmlToAnnotatedString(uiState.translation!!, secondaryTextStyle),
          modifier = Modifier.padding(top = 8.dp),
          style = secondaryTextStyle,
          textAlign = TextAlign.Center
        )
      }

      Text(
        text = stringResource(
          id = org.quran.ui.R.string.surah_ayah,
          uiState.surahName,
          uiState.ayah
        ),
        style = MaterialTheme.typography.labelSmall,
        color = colorScheme.onSurface.copy(alpha = 0.45f),
        modifier = Modifier
          .padding(top = 16.dp)
          .align(Alignment.CenterHorizontally)
      )
    }
  }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewShareCard(
  @PreviewParameter(
    ShareAyahPreviewParameterProvider::class,
    limit = 1
  ) uiState: ShareAyahUiState,
) {
  QuranTheme {
    ShareAyahScreen(data = uiState, {}, { _ -> })
  }
}
