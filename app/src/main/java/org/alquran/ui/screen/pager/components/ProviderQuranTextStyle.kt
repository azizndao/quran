package org.alquran.ui.screen.pager.components

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import org.alquran.ui.theme.LocalQuranTextStyle
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

            oldStyle.copy(
                fontFamily = fontFamily,
                fontSize = when (fontScale) {
                    FontScale.UNRECOGNIZED, FontScale.NORMAL -> 24f
                    FontScale.SMALL -> 20f
                    FontScale.LARGE -> 26f
                }.sp
            )
        }
    }

    CompositionLocalProvider(LocalQuranTextStyle provides style, content = content)
}
