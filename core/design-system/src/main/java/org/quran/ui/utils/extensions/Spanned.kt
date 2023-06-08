package org.quran.ui.utils.extensions

import android.graphics.Typeface
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.text.style.UnderlineSpan
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.text.HtmlCompat

fun String.htmlToAnnotatedString(
  colorScheme: ColorScheme,
  style: TextStyle,
  highLightBold: Boolean = false
): AnnotatedString {
  return HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_COMPACT)
    .toAnnotatedString(colorScheme, style, highLightBold)
}

@Composable
fun htmlToAnnotatedString(
  html: String,
  style: TextStyle = MaterialTheme.typography.bodyMedium,
  highLightBold: Boolean = false
): AnnotatedString {
  val colorScheme = MaterialTheme.colorScheme
  return remember(html) {
    html.htmlToAnnotatedString(colorScheme, style, highLightBold)
  }
}

/**
 * Converts a [Spanned] into an [AnnotatedString] trying to keep as much formatting as possible.
 *
 * Currently supports `bold`, `italic`, `underline` and `color`.
 */
fun Spanned.toAnnotatedString(
  colorScheme: ColorScheme,
  style: TextStyle,
  highLightBold: Boolean = false
) = buildAnnotatedString {
  val spanned = this@toAnnotatedString
  append(spanned.toString())

  getSpans(0, spanned.length, Any::class.java).forEach { span ->
    val start = getSpanStart(span)
    val end = getSpanEnd(span)
    when (span) {
      is StyleSpan -> when (span.style) {
        Typeface.BOLD -> addStyle(
          SpanStyle(
            fontWeight = FontWeight.Bold,
            color = if (highLightBold) colorScheme.primary else colorScheme.onSurface
          ), start, end
        )

        Typeface.ITALIC -> addStyle(SpanStyle(fontStyle = FontStyle.Italic), start, end)
        Typeface.BOLD_ITALIC -> addStyle(
          SpanStyle(
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic
          ), start, end
        )
      }

      is UnderlineSpan -> addStyle(
        SpanStyle(textDecoration = TextDecoration.Underline),
        start,
        end
      )

      is ForegroundColorSpan -> addStyle(
        SpanStyle(color = Color(span.foregroundColor)),
        start,
        end
      )

      is SuperscriptSpan -> addStyle(
        SpanStyle(
          baselineShift = BaselineShift.Superscript,
          color = colorScheme.secondary,
          fontSize = style.fontSize * 0.75
        ),
        start,
        end
      )

      is SubscriptSpan -> addStyle(
        SpanStyle(
          baselineShift = BaselineShift.Subscript,
          color = colorScheme.secondary,
          fontSize = style.fontSize * 0.75
        ),
        start,
        end
      )
    }
  }
}
