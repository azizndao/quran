package org.alquran.ui.screen.pager

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import arg.quran.models.audio.WordSegment
import arg.quran.models.quran.VerseKey
import org.alquran.ui.uistate.MushafPage
import org.koin.androidx.compose.get
import org.muslimapp.core.audio.PlaybackConnection
import timber.log.Timber


@Composable
fun rememberPlayingAya(): State<VerseKey?> {
  val wordSegment = LocalPlayingWordProvider.current

  return remember {
    derivedStateOf {
      wordSegment?.let { VerseKey(it.sura, it.aya) }
    }
  }
}

@Composable
fun rememberPlayingWord(): WordSegment? {
  return LocalPlayingWordProvider.current
}

@Composable
fun rememberPlayingWordInLine(line: MushafPage.TextLine): WordSegment? {
  val segment = LocalPlayingWordProvider.current ?: return null
  val wordSegment = segment.takeIf {
    line.words.any {
      it.key.aya == segment.aya && it.key.sura == segment.sura && it.position == segment.position
    }
  }
  Timber.e(segment.toString())
  return wordSegment
}

@Composable
fun rememberPlayingWord(verseKey: VerseKey): State<WordSegment?> {
  val playingWord = LocalPlayingWordProvider.current
  return remember {
    derivedStateOf {
      playingWord?.takeIf { it.sura == verseKey.sura && it.aya == verseKey.aya }
    }
  }
}

@Composable
fun rememberPlayingWord(line: MushafPage.TextLine): State<WordSegment?> {
  val playingWord = LocalPlayingWordProvider.current
  return remember {
    derivedStateOf {
      playingWord?.takeIf { line.words.any { it.key.aya == playingWord.aya && it.key.sura == playingWord.sura && it.position == playingWord.position } }
    }
  }
}

val LocalPlayingWordProvider = compositionLocalOf<WordSegment?> { null }

@Composable
fun ProviderCurrentPlayingWord(
  content: @Composable () -> Unit
) {
  if (LocalInspectionMode.current) {
    CompositionLocalProvider(LocalPlayingWordProvider provides null, content = content)
  } else {
    val connection: PlaybackConnection = get()

    val word by connection.wordTiming.collectAsStateWithLifecycle()

    CompositionLocalProvider(LocalPlayingWordProvider provides word, content = content)
  }
}