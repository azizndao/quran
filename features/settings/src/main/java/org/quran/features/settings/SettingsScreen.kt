package org.quran.features.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.quran.ui.R
import org.quran.ui.components.BackButton
import org.quran.ui.components.MuslimsTopAppBarDefaults

@Composable
internal fun SettingsScreen(
  viewModel: SettingsViewModel,
  popBackStack: () -> Unit,
) {
  Column(modifier = Modifier.fillMaxSize()) {
    TopAppBar(
      title = { Text(stringResource(id = R.string.settings)) },
      navigationIcon = { BackButton(onClick = popBackStack) },
      colors = MuslimsTopAppBarDefaults.smallTopAppBarColors()
    )

    Column(
      modifier = Modifier
        .weight(1f)
        .verticalScroll(rememberScrollState())
        .navigationBarsPadding()
        .padding(bottom = 24.dp)
    ) {

      val listItemColors =
        ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.background)

      SettingsSectionTitle(stringResource(id = R.string.appearance))

      ListItem(
        headlineContent = { Text(text = stringResource(id = R.string.theme_mode)) },
        colors = listItemColors,
      )

      ListItem(
        headlineContent = { Text(text = stringResource(id = R.string.dynamic_color_palette)) },
        colors = listItemColors,
      )

      ListItem(
        headlineContent = { Text(text = stringResource(id = R.string.color_palette)) },
        colors = listItemColors,
      )

      SettingsSectionTitle(stringResource(id = R.string.quran))

      ListItem(
        headlineContent = { Text(text = stringResource(id = R.string.translations)) },
        colors = listItemColors,
      )

      ListItem(
        headlineContent = { Text(text = stringResource(id = R.string.font)) },
        colors = listItemColors,
      )

      SettingsSectionTitle(stringResource(id = R.string.audio))

      ListItem(
        headlineContent = { Text(text = stringResource(id = R.string.download_manager)) },
        colors = listItemColors,
      )

      ListItem(
        headlineContent = { Text(text = stringResource(id = R.string.streaming_audio)) },
        colors = listItemColors,
      )

      ListItem(
        headlineContent = { Text(text = stringResource(id = R.string.data_and_storage)) },
        colors = listItemColors,
      )

      SettingsSectionTitle(stringResource(id = R.string.others))

      ListItem(
        headlineContent = { Text(text = stringResource(id = R.string.term_of_services)) },
        colors = listItemColors,
      )

      ListItem(
        headlineContent = { Text(text = stringResource(id = R.string.open_source_licenses)) },
        colors = listItemColors,
      )

      ListItem(
        headlineContent = { Text(text = stringResource(id = R.string.about)) },
        colors = listItemColors,
      )
    }
  }
}

@Composable
fun SettingsSectionTitle(name: String) {
  val style = MaterialTheme.typography.labelLarge.copy(
    color = MaterialTheme.colorScheme.primary
  )
  Text(text = name, style = style, modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))
}