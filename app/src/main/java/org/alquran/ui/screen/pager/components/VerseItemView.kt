package org.alquran.ui.screen.pager.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.core.text.HtmlCompat.fromHtml
import org.alquran.R
import org.alquran.audio.models.AudioState
import org.alquran.hafs.model.Verse
import org.alquran.ui.theme.quran
import org.alquran.ui.theme.translation
import org.alquran.utils.extensions.toAnnotatedString
import org.quram.common.model.VerseKey
import org.quran.network.model.CharType

@Composable
fun VerseItemView(
    modifier: Modifier = Modifier,
    verse: Verse,
    audioState: AudioState,
) {
    val background by getPlayingBackground(audioState)

    val text = remember {
        buildAnnotatedString {
            for (word in verse.words) {
                append(word.text)
                if (word.charType != CharType.End) append(" ")
            }
        }
    }

    Text(
        text = text,
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
    translation: org.alquran.hafs.model.VerseTranslation,
    audioState: AudioState,
) {
    val colorScheme = MaterialTheme.colorScheme

    val background by getPlayingBackground(audioState)

    val style = MaterialTheme.typography.translation

    val text = remember(translation) {
        buildAnnotatedString {
            val html = fromHtml(translation.text, FROM_HTML_MODE_COMPACT)
            append(html.toAnnotatedString(colorScheme, style))
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind { drawRect(background) }
            .padding(top = 18.dp, start = 24.dp, end = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "— " + translation.authorName,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Normal,
                color = LocalContentColor.current.copy(alpha = 0.7f)
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
    verseKey: VerseKey,
    isBookmarked: Boolean,
    audioState: AudioState,
    onBookmarkChange: () -> Unit,
    onPlay: () -> Unit,
    onMoreClick: () -> Unit,
) {
    CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colorScheme.secondary
    ) {
        val background by getPlayingBackground(audioState)
        Row(
            modifier = modifier
                .fillMaxWidth()
                .drawBehind { drawRect(background) }
                .padding(bottom = 12.dp, top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = verseKey.toString(),
                modifier = Modifier
                    .padding(start = 24.dp, end = 8.dp)
                    .background(
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(0.13f),
                        MaterialTheme.shapes.small
                    )
                    .padding(8.dp, 4.dp)
                    .align(Alignment.CenterVertically)
            )

            BookmarkButton(
                isBookmarked = isBookmarked,
                onClick = { onBookmarkChange() },
            )

            PlayButton(
                audioState = audioState,
                onPlay = onPlay
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = onMoreClick,
                modifier = Modifier.padding(horizontal = 8.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_more_vert),
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
    audioState: AudioState,
    colorScheme: ColorScheme = MaterialTheme.colorScheme
) = animateColorAsState(
    when (audioState) {
        AudioState.PLAYING -> colorScheme.surfaceColorAtElevation(2.dp)
        else -> colorScheme.surface
    },
)

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
private fun PlayButton(
    modifier: Modifier = Modifier,
    audioState: AudioState,
    onPlay: () -> Unit,
) {

    when (audioState) {
        AudioState.LOADING -> CircularProgressIndicator(
            modifier = modifier.size(IconSize),
            strokeWidth = 2.5.dp
        )

        else -> {
            val icon = AnimatedImageVector.animatedVectorResource(R.drawable.ic_play_pause)

            IconButton(onClick = onPlay) {
                Icon(
                    painter = rememberAnimatedVectorPainter(icon, audioState == AudioState.PLAYING),
                    contentDescription = null,
                    modifier = Modifier.size(IconSize),
                )
            }
        }
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

    val iconScale by translation.animateFloat(label = "icon scale transition",
        transitionSpec = { spring(Spring.DampingRatioMediumBouncy) }) { isPressed ->
        if (isPressed) 2f else 1f
    }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            pressed = interaction is PressInteraction.Press
        }
    }

    IconButton(modifier = modifier, onClick = onClick, interactionSource = interactionSource) {
        val drawableId =
            if (isBookmarked) R.drawable.ic_baseline_bookmark else R.drawable.ic_bookmark
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
