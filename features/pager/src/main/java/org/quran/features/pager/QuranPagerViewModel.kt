package org.quran.features.pager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import arg.quran.models.quran.VerseKey
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.quram.common.core.QuranInfo
import org.quram.common.utils.QuranDisplayData
import org.quran.bookmarks.models.Bookmark
import org.quran.bookmarks.models.BookmarkTag
import org.quran.bookmarks.repository.BookmarkRepository
import org.quran.core.audio.PlaybackConnection
import org.quran.datastore.DisplayMode
import org.quran.datastore.repositories.QuranPreferencesRepository
import org.quran.features.pager.uiState.DialogUiState
import org.quran.features.pager.uiState.QuranEvent
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
  private val getTranslationPage: GetTranslationPage,
  private val getMushafPage: GetMushafPage,
  private val bookmarkRepository: BookmarkRepository,
  private val quranDisplayData: QuranDisplayData,
  translationRepository: TranslationsRepository
) : ViewModel() {

  val args = quranPagerDestinationArgs(savedStateHandle)

  val nowPlayingFlow =
    playbackConnection.nowPlaying.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

  val playingPageFlow = playbackConnection.nowPlaying.map { key ->
    key?.let { quranInfo.getPageFromSuraAyah(it.sura, it.ayah) }
  }.distinctUntilChanged().filterNotNull()


  private val selectionFlow = MutableStateFlow(
    if (args.verseKey != null) QuranSelection.InitialVerse(args.verseKey) else QuranSelection.None
  )

  var isFullscreen by mutableStateOf(false)
    private set

  var dialogUiState: DialogUiState by mutableStateOf(DialogUiState.None)
    private set

  val uiStateFlow = combine(
    quranPreferences.getAllPreferences(),
    translationRepository.observeSelectedEditions(),
  ) { preferences, locales ->
    QuranPagerUiState(
      page = args.page,
      displayMode = preferences.displayMode,
      quranFontScale = preferences.quranFontScale,
      translationFontScale = preferences.translationFontScale,
      version = preferences.fontVersion,
      exception = if (locales.isEmpty()) NoTranslationException() else null,
    )
  }.distinctUntilChanged()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), QuranPagerUiState(page = args.page))

  init {
    getTranslationPage.coroutineScope = viewModelScope
    viewModelScope.launch {
      delay(5000)
      if (selectionFlow.value is QuranSelection.InitialVerse) {
        selectionFlow.value = QuranSelection.None
      }
    }
  }

  fun toggleFullscreen() {
    isFullscreen = !isFullscreen
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

  fun onEvent(event: QuranEvent) {
    when (event) {
      is QuranEvent.ToggleBookmark -> viewModelScope.launch {
        dialogUiState = if (event.isBookmark) {
          showBookmarkTagUiState(event.verse)
        } else {
          selectBookmarkTagUiState(event.verse)
        }
      }

      is QuranEvent.Play -> playbackConnection.onPlaySurah(event.key)

      is QuranEvent.AyahLongPressed -> viewModelScope.launch {
        selectionFlow.value = QuranSelection.Highlight(event.key, event.word, event.bookmarked)
        dialogUiState = DialogUiState.VerseMenu(event.key, event.bookmarked)
      }

      QuranEvent.AyahPressed -> {
        if (selectionFlow.value == QuranSelection.None) {
          toggleFullscreen()
        } else {
          selectionFlow.update { QuranSelection.None }
        }
      }

      is QuranEvent.Selection -> selectionFlow.value = event.selection
      QuranEvent.PlayOrPause -> playbackConnection.playOrPause()
      QuranEvent.SkipToNext -> playbackConnection.skipToNextSurah()
      QuranEvent.SkipToPrevious -> playbackConnection.skipToPreviousSurah()
      is QuranEvent.VerseNote -> dialogUiState = DialogUiState.CreateNote(event.key)

      is QuranEvent.VerseTafsir -> dialogUiState = DialogUiState.VerseTafsir(event.key)
    }
  }

  private suspend fun showBookmarkTagUiState(verse: VerseKey): DialogUiState {
    return DialogUiState.ShowBookmarks(
      verse = verse,
      tags = bookmarkRepository.getAllTagfsForVerse(verse).toPersistentList()
    )
  }

  private suspend fun selectBookmarkTagUiState(key: VerseKey) = DialogUiState.SelectBookmarkTab(
    tags = bookmarkRepository.getAllTags().toPersistentList(),
    verse = key
  )

  fun changeDisplayMode(mode: DisplayMode) {
    viewModelScope.launch {
      quranPreferences.setDisplayMode(mode)
    }
  }

  fun onDismissRequest() {
    dialogUiState = DialogUiState.None
    selectionFlow.value = QuranSelection.None
  }

  fun createNote(verse: VerseKey, note: String) {
    onDismissRequest()
  }

  fun removeBookmark(verse: VerseKey, tag: BookmarkTag) {
    onDismissRequest()
    viewModelScope.launch {
      bookmarkRepository.removeBookmark(verse, tag.id)
    }
  }

  fun onCreateBookmarkAndTag(verse: VerseKey, tagName: String) {
    onDismissRequest()
    viewModelScope.launch(Dispatchers.IO) {
      val tag = bookmarkRepository.addReturnTag(tagName)
      bookmarkRepository.addBookmark(
        Bookmark(
          key = verse,
          label = quranDisplayData.getAyahString(verse.sura, verse.aya),
          tagId = tag.id
        )
      )
    }
  }

  fun onCreateBookmark(verse: VerseKey, tagId: Int) {
    onDismissRequest()
    viewModelScope.launch(Dispatchers.IO) {
      bookmarkRepository.addBookmark(
        Bookmark(
          key = verse,
          label = quranDisplayData.getAyahString(verse.sura, verse.aya),
          tagId = tagId
        )
      )
    }
  }
}

