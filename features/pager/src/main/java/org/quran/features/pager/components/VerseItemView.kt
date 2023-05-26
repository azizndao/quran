package org.quran.features.pager.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.core.text.HtmlCompat.fromHtml
import org.quran.ui.theme.quran
import org.quran.ui.theme.translation
import org.quran.features.pager.uiState.QuranEvent
import org.quran.features.pager.uiState.TranslationPage
import org.quran.ui.utils.extensions.toAnnotatedString

@Composable
fun VerseItemView(
  modifier: Modifier = Modifier,
  verse: TranslationPage.Verse,
) {
  val background by getPlayingBackground(verse.isPlaying)

  Text(
    text = verse.text,
    style = MaterialTheme.typography.quran,
    modifier = modifier
      .drawBehind { drawRect(background) }
      .padding(start = 24.dp, top = 24.dp, end = 24.dp)
      .fillMaxWidth()
  )
}

@Composable
fun TranslationItemView(
  modifier: Modifier = Modifier,
  translation: TranslationPage.TranslatedVerse,
) {
  val colorScheme = MaterialTheme.colorScheme

  val background by getPlayingBackground(translation.isPlaying)

  val style = MaterialTheme.typography.translation

  val text = remember(translation) {
    buildAnnotatedString {
      val html = fromHtml(translation.text, FROM_HTML_MODE_COMPACT)
      append(html.toAnnotatedString(colorScheme, style))
    }
  }

  Column(modifier = modifier
    .fillMaxWidth()
    .drawBehind { drawRect(background) }
    .padding(top = 18.dp, start = 24.dp, end = 24.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)) {
    Text(
      text = "— ${translation.authorName}",
      style = MaterialTheme.typography.labelSmall.copy(
        fontWeight = FontWeight.Normal, color = LocalContentColor.current.copy(alpha = 0.7f)
      ),
    )

    SelectionContainer {
      Text(text = text, style = style)
    }
  }
}

private val IconSize = 20.dp

@Composable
fun VerseToolbarView(
  modifier: Modifier = Modifier,
  verse: TranslationPage.VerseToolbar,
  onEvent: (QuranEvent) -> Unit
) {
  CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.secondary) {
    val background by getPlayingBackground(verse.isPlaying)

    Row(modifier = modifier
      .fillMaxWidth()
      .drawBehind { drawRect(background) }
      .padding(bottom = 12.dp, top = 12.dp), verticalAlignment = Alignment.CenterVertically) {

      Text(
        text = verse.suraAyah.toString(),
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier
          .padding(start = 24.dp, end = 16.dp)
          .padding(vertical = 4.dp)
          .align(Alignment.CenterVertically)
      )

      BookmarkButton(isBookmarked = verse.isBookmarked) {
        onEvent(QuranEvent.ToggleBookmark(verse.suraAyah, verse.isBookmarked))
      }

      PlayButton(isPlaying = verse.isPlaying) {
        onEvent(QuranEvent.Play(verse.suraAyah))
      }

      Spacer(modifier = Modifier.weight(1f))

      IconButton(
        onClick = { onEvent(QuranEvent.AyahLongPressed(verse.suraAyah, null, verse.isBookmarked)) },
        modifier = Modifier.padding(horizontal = 8.dp),
      ) {
        Icon(
          painter = painterResource(id = org.quran.ui.R.drawable.ic_more_vert),
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(IconSize),
        )
      }
    }
  }
}

@Composable
private fun getPlayingBackground(
  isPlaying: Boolean, colorScheme: ColorScheme = MaterialTheme.colorScheme
): State<Color> = animateColorAsState(
  when {
    isPlaying -> colorScheme.surfaceColorAtElevation(2.dp)
    else -> colorScheme.surface
  },
  label = "getPlayingBackground",
)

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
private fun PlayButton(
  modifier: Modifier = Modifier,
  isPlaying: Boolean,
  onPlay: () -> Unit,
) {

  val icon = AnimatedImageVector.animatedVectorResource(org.quran.ui.R.drawable.ic_play_pause)

  IconButton(onClick = onPlay, modifier = modifier) {
    Icon(
      painter = rememberAnimatedVectorPainter(icon, isPlaying),
      contentDescription = null,
      modifier = Modifier.size(IconSize),
    )
  }
}

@Composable
private fun BookmarkButton(
  modifier: Modifier = Modifier,
  isBookmarked: Boolean, onClick: () -> Unit,
) {

  val interactionSource = remember { MutableInteractionSource() }

  var pressed by remember { mutableStateOf(false) }
  val translation = updateTransition(targetState = pressed, label = "bookmark icon transition")

  val iconScale by translation.animateFloat(
    label = "icon scale transition",
    transitionSpec = { spring(Spring.DampingRatioMediumBouncy) }) { isPressed ->
    if (isPressed) 2f else 1f
  }

  LaunchedEffect(interactionSource) {
    interactionSource.interactions.collect { interaction ->
      pressed = interaction is PressInteraction.Press
    }
  }

  IconButton(modifier = modifier, onClick = onClick, interactionSource = interactionSource) {
    val drawableId = when {
      isBookmarked -> org.quran.ui.R.drawable.bookmark_added
      else -> org.quran.ui.R.drawable.bookmark_add
    }

    Icon(
      painterResource(id = drawableId),
      contentDescription = null,
      modifier = Modifier
        .size(IconSize)
        .scale(iconScale),
    )
  }
}

//@Preview(showBackground = true)
//@Preview(
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
//)
//@Composable
//fun AyahItemPreview(
//    @PreviewParameter(
//        AyahPreviewParameterProvider::class, limit = 2
//    ) ayah: Ayah,
//) {
//
//    val uiState by remember {
//        mutableStateOf(
//            TranslationPage.Ayah(
//                ayah,
//                isBookmarked = true,
//                highLighted = false,
//                audioState = AudioState.PLAYING
//            )
//        )
//    }
//
//    MuslimsTheme {
//        Surface {
//            AyahItemView(uiState = uiState) {}
//        }
//    }
//}
