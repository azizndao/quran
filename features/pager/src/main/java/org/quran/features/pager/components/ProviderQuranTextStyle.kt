package org.quran.features.pager.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.sp
import org.quran.datastore.FontScale
import org.quran.ui.R
import org.quran.ui.theme.LocalQuranTextStyle


@Composable
fun ProviderQuranTextStyle(
  page: Int,
  fontScale: FontScale = FontScale.NORMAL,
  content: @Composable () -> Unit
) {

  val oldStyle = LocalQuranTextStyle.current

  val context = LocalContext.current

  val style by remember(fontScale, page, oldStyle) {
    derivedStateOf {
      val fontFamily = FontFamily(
        Font(
          "v1/fonts/ttf/p${page}.ttf",
          assetManager = context.assets,
        )
      )

      val fontSize = when (fontScale) {
        FontScale.UNRECOGNIZED, FontScale.NORMAL -> 24.75.sp
        FontScale.SMALL -> 20.sp
        FontScale.LARGE -> 26.sp
      }

      oldStyle.copy(
        fontFamily = fontFamily,
        fontSize = fontSize,
        lineHeight = fontSize * 1.8,
        platformStyle = PlatformTextStyle(includeFontPadding = false),
      )
    }
  }

  CompositionLocalProvider(LocalQuranTextStyle provides style, content = content)
}

@Composable
fun ProviderPage77QuranTextStyle(
  content: @Composable () -> Unit
) {

  val style = remember {
    TextStyle(
      fontFamily = FontFamily(Font(R.font.p77)),
      fontSize = 24.75.sp,
      lineHeight = 24.75.sp * 1.8,
      platformStyle = PlatformTextStyle(includeFontPadding = false),
      textDirection = TextDirection.Rtl
    )
  }

  CompositionLocalProvider(LocalQuranTextStyle provides style, content = content)
}


@Composable
fun ProviderPage604QuranTextStyle(
  content: @Composable () -> Unit
) {

  val style = remember {
    TextStyle(
      fontFamily = FontFamily(Font(R.font.p604)),
      fontSize = 24.75.sp,
      lineHeight = 24.75.sp * 1.8,
      platformStyle = PlatformTextStyle(includeFontPadding = false),
      textDirection = TextDirection.Rtl
    )
  }

  CompositionLocalProvider(LocalQuranTextStyle provides style, content = content)
}
