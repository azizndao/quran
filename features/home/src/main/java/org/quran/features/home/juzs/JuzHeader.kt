package org.quran.features.home.juzs

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.quran.ui.theme.QuranTheme

@Composable
fun JuzHeader(juz: Int, page: Int, modifier: Modifier = Modifier) {

  Row(
    horizontalArrangement = Arrangement.SpaceBetween,
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {

    Text(
      stringResource(id = org.quran.ui.R.string.juz_item, juz),
      style = MaterialTheme.typography.labelLarge,
      color = MaterialTheme.colorScheme.primary
    )

    Text(
      page.toString(),
      style = MaterialTheme.typography.labelSmall,
      color = MaterialTheme.colorScheme.primary
    )
  }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview
@Composable
fun HuzHeaderPreview() {
  QuranTheme {
    JuzHeader(juz = 2, 22)
  }
}