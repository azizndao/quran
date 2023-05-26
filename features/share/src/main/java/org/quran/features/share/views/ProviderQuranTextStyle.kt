package org.quran.features.share.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import org.quran.ui.theme.LocalQuranTextStyle
import org.quran.datastore.FontScale


@Composable
fun ProviderQuranTextStyle(
  page: Int,
  fontScale: FontScale,
  content: @Composable () -> Unit
) {

  val oldStyle = LocalQuranTextStyle.current

  val context = LocalContext.current

  val style by remember(fontScale, page, oldStyle) {
    derivedStateOf {
      val fontFamily = FontFamily(
        Font("v1/fonts/ttf/p${page}.ttf", assetManager = context.assets)
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
        textAlign = TextAlign.Justify,
      )
    }
  }

  CompositionLocalProvider(LocalQuranTextStyle provides style, content = content)
}
