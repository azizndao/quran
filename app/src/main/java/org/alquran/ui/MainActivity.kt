package org.alquran.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.core.view.WindowCompat
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityRetainedScope
import org.koin.androidx.scope.activityScope
import org.koin.core.scope.Scope
import org.quran.core.audio.PlaybackConnection
import org.quran.ui.theme.QuranFontFamilies
import org.quran.ui.theme.QuranTheme

class MainActivity : ComponentActivity(), AndroidScopeComponent {

  override val scope: Scope by activityScope()

  private val playbackConnection by inject<PlaybackConnection>()

  override fun onCreate(savedInstanceState: Bundle?) {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    super.onCreate(savedInstanceState)
    lifecycle.addObserver(playbackConnection)
    setContent {
      CompositionLocalProvider(
        LocalFontFamilyResolver provides createFontFamilyResolver(
          LocalContext.current, QuranFontFamilies.handler
        ),
      ) {
        QuranTheme {
          Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
          ) {
            QuranApp(playbackConnection)
          }
        }
      }
    }
  }
}