package org.quran.features.pager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.quram.common.core.QuranInfo
import org.quran.bookmarks.model.Bookmark
import org.quran.bookmarks.repository.BookmarkRepository
import org.quran.core.audio.PlaybackConnection
import org.quran.datastore.DisplayMode
import org.quran.datastore.repositories.AudioPreferencesRepository
import org.quran.datastore.repositories.QuranPreferencesRepository
import org.quran.datastore.serializers.DEFAULT_QURAN_FONT_SIZE
import org.quran.datastore.serializers.DEFAULT_TRANSLATION_FONT_SIZE
import org.quran.features.pager.uiState.QuranPageItem
import org.quran.features.pager.uiState.QuranPagerUiState
import org.quran.features.pager.uiState.QuranSelection
import org.quran.features.pager.useCase.GetMushafPage
import org.quran.features.pager.useCase.GetTranslationPage
import org.quran.translation.exceptions.NoTranslationException
import org.quran.translation.repositories.TranslationsRepository
import timber.log.Timber

internal class QuranPagerViewModel(
  savedStateHandle: SavedStateHandle,
  private val quranInfo: QuranInfo,
  private val playbackConnection: PlaybackConnection,
  private val quranPreferences: QuranPreferencesRepository,
  private val audioSettings: AudioPreferencesRepository,
  private val getTranslationPage: GetTranslationPage,
  private val getMushafPage: GetMushafPage,
  private val bookmarkRepository: BookmarkRepository,
  translationRepository: TranslationsRepository
) : ViewModel() {

  private val args = quranPagerDestinationArgs(savedStateHandle)

  val nowPlayingFlow =
    playbackConnection.nowPlaying.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

  private val playingPageFlow = playbackConnection.nowPlaying.map { key ->
    key?.let { quranInfo.getPageFromSuraAyah(it.sura, it.ayah) }
  }.distinctUntilChanged()

  private val fullscreenFlow = MutableStateFlow(false)

  private val selectionFlow = MutableStateFlow(
    if (args.verseKey != null) QuranSelection.InitialVerse(args.verseKey) else QuranSelection.None
  )

  val uiStateFlow = combine(
    quranPreferences.getAllPreferences(),
    playingPageFlow,
    fullscreenFlow,
    translationRepository.observeSelectedEditions(),
  ) { preferences, page, fullscreen, locales ->
    QuranPagerUiState(
      page = args.page,
      playingPage = page,
      displayMode = preferences.displayMode,
      quranFontScale = preferences.quranFontScale,
      translationFontScale = preferences.translationFontScale,
      version = preferences.fontVersion,
      onEvent = ::onEvent,
      isFullscreen = fullscreen,
      exception = if (locales.isEmpty()) NoTranslationException() else null,
    )
  }.distinctUntilChanged().stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5_000),
    initialValue = QuranPagerUiState(
      page = args.page,
      playingPage = null,
      displayMode = DisplayMode.UNRECOGNIZED,
      quranFontScale = DEFAULT_QURAN_FONT_SIZE,
      translationFontScale = DEFAULT_TRANSLATION_FONT_SIZE,
      version = 1,
      onEvent = ::onEvent,
    )
  )

  init {
    getTranslationPage.coroutineScope = viewModelScope
    viewModelScope.launch {
      delay(5000)
      if (selectionFlow.value is QuranSelection.InitialVerse) {
        selectionFlow.value = QuranSelection.None
      }
    }
  }

  @Composable
  fun pageFactory(mode: DisplayMode, page: Int, version: Int): QuranPageItem? {
    val itemFlow = remember(mode, page) {

      when (mode) {
        DisplayMode.QURAN_TRANSLATION -> {
          val selectedVerse = when (val s = selectionFlow.value) {
            is QuranSelection.InitialVerse -> s.key
            is QuranSelection.Highlight -> s.key
            else -> null
          }
          getTranslationPage(page, selectedVerse, version).catch { Timber.e(it) }
        }

        DisplayMode.QURAN -> getMushafPage(page, version, selectionFlow)
        DisplayMode.UNRECOGNIZED -> flowOf(null)
      }.flowOn(Dispatchers.IO).stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), null
      )
    }

    val itemState by itemFlow.collectAsStateWithLifecycle()

    return itemState
  }

  private fun onEvent(event: QuranEvent) {
    viewModelScope.launch(Dispatchers.IO) {
      when (event) {
        is QuranEvent.ToggleBookmark -> if (event.isBookmark) {
          bookmarkRepository.removeBookmark(event.verseKey)
        } else {
          bookmarkRepository.addBookmark(Bookmark(key = event.verseKey, name = "Bookmarks"))
        }

        is QuranEvent.UpdateSelection -> {}

        is QuranEvent.Play -> playbackConnection.onPlaySurah(
          sura = event.verseKey.sura,
          reciterId = audioSettings.getCurrentReciter().first(),
          startAya = event.verseKey.aya
        )

        is QuranEvent.AyahLongPressed -> selectionFlow.value =
          QuranSelection.Highlight(event.verseKey, event.word, event.bookmarked)

        QuranEvent.AyahPressed -> {
          if (selectionFlow.value == QuranSelection.None) {
            fullscreenFlow.update { !it }
          } else {
            selectionFlow.update { QuranSelection.None }
          }
        }

        is QuranEvent.ChangeDisplayMode -> quranPreferences.setDisplayMode(event.mode)
        is QuranEvent.FullscreenChange -> fullscreenFlow.value = event.value
        is QuranEvent.Selection -> selectionFlow.value = event.selection
        QuranEvent.PlayOrPause -> playbackConnection.playOrPause()
        QuranEvent.SkipToNext -> playbackConnection.skipToNextSurah()
        QuranEvent.SkipToPrevious -> playbackConnection.skipToPreviousSurah()
      }
    }
  }
}

