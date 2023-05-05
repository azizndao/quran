package org.alquran.ui.screen.pager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.alquran.audio.models.AudioState
import org.alquran.ui.uistate.PagerUiState
import org.alquran.ui.uistate.QuranPageItem
import org.alquran.ui.uistate.Selection
import org.alquran.usecases.GetMushafPage
import org.alquran.usecases.GetTranslationPage
import org.muslimapp.core.audio.PlaybackConnection
import org.quram.common.core.QuranInfo
import org.quran.bookmarks.model.Bookmark
import org.quran.bookmarks.repository.BookmarkRepository
import org.quran.datastore.DisplayMode
import org.quran.datastore.repositories.AudioPreferencesRepository
import org.quran.datastore.repositories.QuranPreferencesRepository
import org.quran.datastore.serializers.DEFAULT_QURAN_FONT_SIZE
import org.quran.datastore.serializers.DEFAULT_TRANSLATION_FONT_SIZE
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
) : ViewModel() {

  private val args = quranPagerDestinationArgs(savedStateHandle)

  private val playingPage: Flow<Int?> = combine(
    playbackConnection.playingState,
    playbackConnection.nowPlaying,
  ) { state, mediaItem ->
    when (state) {
      AudioState.PLAYING -> mediaItem?.let { quranInfo.getPageFromSuraAyah(it.sura, it.ayah) }
      else -> null
    }
  }.distinctUntilChanged()

  private val fullscreenFlow = MutableStateFlow(false)
  private val _selection = MutableStateFlow<Selection>(
    if (args.verseKey != null) Selection.InitialVerse(args.verseKey) else Selection.None
  )


  val uiState = combine(
    quranPreferences.getAllPreferences(),
    playingPage,
    fullscreenFlow,
    _selection,
  ) { preferences, page, fullscreen, selectedAyah ->
    PagerUiState(
      initialPage = args.page,
      playingPage = page,
      displayMode = preferences.displayMode,
      quranFontScale = preferences.quranFontScale,
      translationFontScale = preferences.translationFontScale,
      version = preferences.fontVersion,
      onDisplayModeChange = ::onDisplayMode,
      onAyahEvent = ::onAyahEvent,
      onFullscreen = { fullscreenFlow.value = it },
      isFullscreen = fullscreen,
      selection = selectedAyah,
      setSelection = { _selection.value = it }
    )
  }.distinctUntilChanged()
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5_000),
      initialValue = PagerUiState(
        args.page,
        null,
        DisplayMode.UNRECOGNIZED,
        quranFontScale = DEFAULT_QURAN_FONT_SIZE,
        translationFontScale = DEFAULT_TRANSLATION_FONT_SIZE,
        version = 1,
        onDisplayModeChange = ::onDisplayMode,
        onAyahEvent = ::onAyahEvent,
        onFullscreen = { fullscreenFlow.value = it },
        selection = _selection.value,
        setSelection = { _selection.value = it }
      )
    )

  init {
    viewModelScope.launch {
      delay(5000)
      if (_selection.value is Selection.InitialVerse) {
        _selection.value = Selection.None
      }
    }
  }

  @Composable
  fun pageFactory(mode: DisplayMode, page: Int, version: Int): QuranPageItem? {
    val itemFlow = remember(mode, page) {

      when (mode) {
        DisplayMode.QURAN_TRANSLATION -> {
          val selectedVerse = when (val s = _selection.value) {
            is Selection.InitialVerse -> s.key
            is Selection.Highlight -> s.key
            else -> null
          }
          getTranslationPage(page, selectedVerse, version)
            .catch { Timber.e(it) }
        }

        DisplayMode.QURAN -> getMushafPage(page, version, _selection)
        DisplayMode.UNRECOGNIZED -> flowOf(null)
      }.flowOn(Dispatchers.IO).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        null
      )
    }

    val itemState by itemFlow.collectAsStateWithLifecycle()

    return itemState
  }

  private fun onAyahEvent(event: AyahEvent) {
    viewModelScope.launch(Dispatchers.IO) {
      when (event) {
        is AyahEvent.ToggleBookmark -> if (event.isBookmark) {
          bookmarkRepository.removeBookmark(event.verseKey)
        } else {
          bookmarkRepository.addBookmark(Bookmark(key = event.verseKey, name = "Bookmarks"))
        }

        is AyahEvent.UpdateSelection -> {}

        is AyahEvent.Play -> playbackConnection.onPlaySurah(
          sura = event.verseKey.sura,
          reciterId = audioSettings.getCurrentReciter().first(),
          startAya = event.verseKey.aya
        )

        is AyahEvent.AyahLongPressed -> {}

        is AyahEvent.AyahPressed -> {}
      }
    }
  }

  private fun onDisplayMode(value: DisplayMode) {
    viewModelScope.launch(Dispatchers.IO) {
      quranPreferences.setDisplayMode(value)
    }
  }
}

