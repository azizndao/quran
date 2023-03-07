package org.alquran.ui.screen.home.reminders

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.alquran.R
import org.muslimapp.feature.quran.ui.LocalInsetsPadding

@Composable
fun RemindersScreen() {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .padding(LocalInsetsPadding.current.asPaddingValues())
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {

      Text(
        text = stringResource(id = R.string.no_reminder),
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
        color = LocalContentColor.current.copy(alpha = 0.5f)
      )
    }

    FloatingActionButton(
      onClick = { /*TODO*/ },
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(end = 16.dp)
    ) {
      Icon(painterResource(id = R.drawable.ic_add), null)
    }
  }
}