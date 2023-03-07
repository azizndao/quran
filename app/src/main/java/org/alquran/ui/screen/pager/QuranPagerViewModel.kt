package org.alquran.ui.screen.pager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import arg.quran.models.quran.VerseKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.alquran.audio.models.AudioState
import org.alquran.ui.uistate.PagerUiState
import org.alquran.ui.uistate.QuranPageItem
import org.alquran.usecases.GetMushafPage
import org.alquran.usecases.GetTranslationPage
import org.muslimapp.core.audio.PlaybackConnection
import org.quram.common.utils.QuranInfo
import org.quran.datastore.DisplayMode
import org.quran.datastore.repositories.AudioPreferencesRepository
import org.quran.datastore.repositories.QuranPreferencesRepository
import org.quran.datastore.serializers.DEFAULT_QURAN_FONT_SIZE
import org.quran.datastore.serializers.DEFAULT_TRANSLATION_FONT_SIZE

internal class QuranPagerViewModel(
  savedStateHandle: SavedStateHandle,
  private val quranInfo: QuranInfo,
  private val playbackConnection: PlaybackConnection,
  private val quranPreferences: QuranPreferencesRepository,
  private val audioSettings: AudioPreferencesRepository,
  private val getTranslationPage: GetTranslationPage,
  private val getMushafPage: GetMushafPage,
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


  val uiState = combine(
    quranPreferences.getAllPreferences(),
    playingPage
  ) { preferences, page ->
    PagerUiState(
      initialPage = args.page,
      playingPage = page,
      displayMode = preferences.displayMode,
      quranFontScale = preferences.quranFontScale,
      translationFontScale = preferences.translationFontScale,
      version = preferences.fontVersion,
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
        version = 1
      )
    )


  private var _selection = MutableStateFlow(args.verseKey)

  init {
    viewModelScope.launch {
      delay(5000)
      _selection.value = null
    }
  }

  @Composable
  fun pageFactory(mode: DisplayMode, page: Int, version: Int): QuranPageItem? {
    val itemFlow = remember(mode, page) {
      when (mode) {
        DisplayMode.QURAN_TRANSLATION -> getTranslationPage(page, _selection.value, version)
        DisplayMode.QURAN -> getMushafPage(page, version)
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

  fun onAyahEvent(event: AyahEvent) {
    viewModelScope.launch(Dispatchers.IO) {
      when (event) {
        is AyahEvent.ToggleBookmark -> if (event.isBookmark) {
//                    bookmarkRepository.removeBookmark(event.verseKey)
        } else {
//                    bookmarkRepository.addBookmark(event.verseKey)
        }

        is AyahEvent.UpdateSelection -> _selection.update { selectionList ->
          val verseKey = VerseKey(event.sura, event.ayah)
          if (verseKey == selectionList) null else verseKey
        }

        is AyahEvent.Play -> withContext(Dispatchers.Main) {
          playbackConnection.onPlaySurah(
            sura = event.verseKey.sura,
            reciterId = audioSettings.getCurrentReciter().first(),
            startAya = event.verseKey.aya
          )
        }

        is AyahEvent.AyahLongPressed -> _selection.update {
          if (it == event.verseKey) {
            null
          } else {
            event.verseKey
          }
        }

        is AyahEvent.AyahPressed -> {
          val selected = event
            .verseKey?.let { isSelected(it.sura, event.verseKey.aya) } ?: false

          if (selected) {
            _selection.update { null }
          }
        }
      }
    }
  }

  fun onDisplayMode(value: DisplayMode) {
    viewModelScope.launch(Dispatchers.IO) {
      quranPreferences.setDisplayMode(value)
    }
  }

  //    @OptIn(DelicateCoroutinesApi::class)
  override fun onCleared() {
//        if (!playbackConnection.isPlaying.value) {
//            GlobalScope.launch { quranPosition.setPosition(args.page) }
//        }
    super.onCleared()
  }

  private fun isSelected(sura: Int, ayah: Int) =
    _selection.value?.aya == ayah && _selection.value?.sura == sura
}
