package org.alquran.ui.screen.pager.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.alquran.R
import org.alquran.ui.screen.translations.directionToQuranTranslations

@Composable
fun NoTranslationView(navigate: (String) -> Unit) {
  Surface {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier
        .fillMaxSize()
        .padding(24.dp)
    ) {
      Text(
        text = stringResource(id = R.string.select_at_least_one_translation),
        style = MaterialTheme.typography.titleLarge
      )

      Spacer(modifier = Modifier.height(24.dp))

      ElevatedButton(onClick = { navigate(directionToQuranTranslations()) }) {
        Text(stringResource(id = R.string.translations))
      }
    }
  }
}