package org.alquran.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.alquran.R
import org.alquran.ui.theme.QuranTheme

@Composable
fun NetworkUnreachableView(
  modifier: Modifier = Modifier,
  onRetry: () -> Unit
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(24.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {

    Icon(
      painter = painterResource(id = R.drawable.ic_round_error_outline_24),
      contentDescription = stringResource(id = R.string.network_error),
      modifier = Modifier.size(42.dp),
      tint = MaterialTheme.colorScheme.error,
    )

    Text(
      text = stringResource(id = R.string.network_error),
      style = MaterialTheme.typography.headlineMedium,
      textAlign = TextAlign.Center
    )

    Button(
      onClick = onRetry,
      contentPadding = PaddingValues(vertical = 8.dp, horizontal = 42.dp)
    ) {
      Text(text = stringResource(id = R.string.retry))
    }
  }
}

@Preview
@Composable
fun NetworkUnreachablePreview() {
  QuranTheme {
    Surface {
      NetworkUnreachableView {

      }
    }
  }
}

@Composable
fun SomethingWrongView() {

}