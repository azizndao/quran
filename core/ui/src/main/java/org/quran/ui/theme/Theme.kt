package org.quran.ui.theme

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import org.alquran.ui.theme.AppTypography
import org.alquran.ui.theme.DarkDefaultThemeColors
import org.alquran.ui.theme.DefaultQuranTextStyle
import org.alquran.ui.theme.LightDefaultThemeColors
import org.alquran.ui.theme.LocalQuranTextStyle
import org.alquran.ui.theme.shapes
import kotlin.math.ln

@SuppressLint("NewApi")
@Composable
fun QuranTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = true,
  arabicTextStyle: TextStyle = DefaultQuranTextStyle,
  content: @Composable () -> Unit,
) {


  var colorScheme = when {
    dynamicColor -> {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      } else {
        if (darkTheme) DarkDefaultThemeColors else LightDefaultThemeColors
      }
    }

    else -> if (darkTheme) DarkDefaultThemeColors else LightDefaultThemeColors
  }

  colorScheme = if (darkTheme) colorScheme.copy(
    surface = colorScheme.surfaceColorAtElevation(colorScheme.surfaceTint, 0.5.dp)
  ) else colorScheme.copy(
    background = colorScheme.surfaceColorAtElevation(colorScheme.tertiary, 1.5.dp)
  )

  if (!LocalInspectionMode.current) {
    val view = LocalView.current
    val window = (view.context as Activity).window
    val insetsController = remember(view) { WindowCompat.getInsetsController(window, view) }

    SideEffect {
      insetsController.isAppearanceLightNavigationBars = !darkTheme
      insetsController.isAppearanceLightStatusBars = !darkTheme
      window.statusBarColor = Color.Transparent.toArgb()
      window.isNavigationBarContrastEnforced = false
      window.navigationBarColor = colorScheme.surface.copy(alpha = 0.94f).toArgb()
    }
  }

  MaterialTheme(
    typography = AppTypography,
    colorScheme = colorScheme,
    shapes = shapes
  ) {
    CompositionLocalProvider(
      LocalQuranTextStyle provides arabicTextStyle,
      content = content
    )
  }
}

fun ColorScheme.surfaceColorAtElevation(
  overlayColor: Color,
  elevation: Dp,
): Color {
  if (elevation == 0.dp) return surface
  val alpha = ((4.5f * ln(elevation.value + 1)) + 2f) / 100f
  return overlayColor.copy(alpha = alpha).compositeOver(surface)
}
