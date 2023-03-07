package org.muslimapp.core.audio

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import arg.quran.models.audio.Qari
import arg.quran.models.audio.WordSegment
import arg.quran.models.audio.aya
import arg.quran.models.audio.sura
import arg.quran.models.quran.VerseKey
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.alquran.audio.models.AudioState
import org.alquran.audio.models.NowPlaying
import org.muslimapp.core.audio.models.MediaId
import org.muslimapp.core.audio.repositories.RecitationRepository
import org.muslimapp.core.audio.repositories.TimingRepository
import org.quran.datastore.repositories.AudioPreferencesRepository
import timber.log.Timber
import java.util.concurrent.Executors

class PlaybackConnection(
  private val context: Context,
  private val recitationRepository: RecitationRepository,
  private val audioSettings: AudioPreferencesRepository,
  private val timingRepository: TimingRepository
) {

  private val job = SupervisorJob()
  private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

  private val _isConnected = MutableStateFlow(false)
  val isConnected = _isConnected.asStateFlow()

  private val _playingState = MutableStateFlow(AudioState.PAUSED)
  val playingState = _playingState.asStateFlow()

  private val _repeatMode = MutableStateFlow(Player.REPEAT_MODE_OFF)
  val repeatMode = _repeatMode.asStateFlow()

  private val _currentMediaItem = MutableStateFlow<MediaItem?>(null)
  val currentMediaItem: StateFlow<MediaItem?> get() = _currentMediaItem


  private suspend fun getPosition() =
    withContext(Dispatchers.Main) { mediaController.currentPosition }

  private fun getPlayingWordFlow(): Flow<WordSegment?> = _currentMediaItem
    .filterNotNull()
    .map { MediaId.fromString(it.mediaId) }
    .distinctUntilChangedBy { mediaId -> mediaId.sura }
    .map { mediaId -> timingRepository.getSuraTimings(mediaId.reciter, mediaId.sura) }
    .transform { timingList ->
      while (true) {
        val aya = timingList.find { getPosition() <= it.duration } ?: break
        emit(aya.segments)
        delay(aya.duration - getPosition())
      }
    }.transform { segments ->
      while (true) {
        val word = segments.findLast { it.endDuration <= getPosition() } ?: break
        emit(word)
        Timber.e(word.toString())
        delay(getPosition() - word.endDuration)
      }
    }

  val wordTiming by lazy {
    getPlayingWordFlow()
      .flowOn(Dispatchers.IO)
      .distinctUntilChanged()
      .stateIn(coroutineScope, SharingStarted.WhileSubscribed(5000), null)
  }

  private val playingAyahFlow: Flow<VerseKey?> = _currentMediaItem
    .map { it?.let { mediaItem -> MediaId.fromString(mediaItem.mediaId) } }
    .distinctUntilChangedBy { it?.sura }
    .map { id -> id?.let { timingRepository.getSuraTimings(it.reciter, id.sura) } }
    .transform { segments ->
      if (segments == null) return@transform emit(null)

      while (true) {
        val timing = segments.find { getPosition() <= it.duration }
        if (timing != null) {
          emit(VerseKey(timing.sura, timing.aya))
          delay(200L)
        }
      }
    }.flowOn(Dispatchers.IO)


  val nowPlaying = playingAyahFlow.combine(_playingState) { verseKey, state ->
    if (verseKey == null) return@combine verseKey

    val currentMediaItem = withContext(Dispatchers.Main) { mediaController.currentMediaItem }
      ?: return@combine null

    val mediaMetadata = currentMediaItem.mediaMetadata
    val mediaId = MediaId.fromString(currentMediaItem.mediaId)

    NowPlaying(
      title = mediaMetadata.title.toString() + " (${verseKey.sura}:${verseKey.aya})",
      reciterName = mediaMetadata.artist.toString(),
      sura = verseKey.sura,
      ayah = verseKey.aya,
      reciter = mediaId.reciter,
      artWork = mediaMetadata.artworkUri,
      position = mediaController.currentPosition,
      bufferedPosition = mediaController.bufferedPosition,
      duration = mediaController.duration,
      state = state,
    )
  }.stateIn(coroutineScope, SharingStarted.Lazily, null)

  private var mediaControllerFuture: ListenableFuture<MediaController>? = null

  private lateinit var mediaController: MediaController

  fun connect() {
    mediaControllerFuture = MediaController.Builder(
      context, SessionToken(context, ComponentName(context, PlaybackService::class.java))
    ).buildAsync()
    mediaControllerFuture?.addListener({
      if (mediaControllerFuture!!.isDone) {
        mediaController = mediaControllerFuture!!.get()
        mediaController.addListener(PlayerListener())
        _isConnected.value = true
      }
    }, Executors.newSingleThreadExecutor())
  }

  fun release() {
    coroutineScope.cancel()
    mediaControllerFuture?.let { MediaController.releaseFuture(it) }
  }

  inner class PlayerListener : Player.Listener {

    override fun onEvents(player: Player, events: Player.Events) {
      if (events.contains(Player.EVENT_REPEAT_MODE_CHANGED)) {
        _repeatMode.value = player.repeatMode
      }

      _playingState.value = when {
        player.isPlaying -> AudioState.PLAYING
        mediaController.isLoading -> AudioState.LOADING
        else -> AudioState.PAUSED
      }
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
      _currentMediaItem.value = mediaItem
    }
  }

  fun onPlaySurah(sura: Int, ayah: Int) {
    coroutineScope.launch {
      val reciter = audioSettings.getCurrentReciter().first()
      onPlaySurah(sura, reciter, ayah)
    }
  }

  fun onPlaySurah(sura: Int, reciterId: String, startAya: Int = 0) {
    coroutineScope.launch {
      val mediaItems = recitationRepository.getRecitations(reciterId)
      val position = timingRepository.getPosition(reciterId, sura, startAya)
      prepareAndPlay(mediaItems, sura, position)
    }
  }

  suspend fun onPlaySurah(
    surah: Int,
    reciter: Qari,
    startAyah: Int = 0,
  ) {
    val mediaItems = recitationRepository.getRecitations(reciter.slug)
    val position = timingRepository.getPosition(reciter.slug, surah, startAyah)
    prepareAndPlay(mediaItems, surah, position)
  }

  private fun prepareAndPlay(mediaItems: List<MediaItem>, surah: Int, position: Long) {
    coroutineScope.launch {
      prepare(mediaItems, surah, position)
      mediaController.play()
    }
  }

  private fun prepare(mediaItems: List<MediaItem>, surah: Int, position: Long) {
    coroutineScope.launch {
      mediaController.setMediaItems(mediaItems, surah - 1, position)
      mediaController.prepare()
    }
  }

  private fun pause() = coroutineScope.launch { mediaController.pause() }

  fun skipToPreviousSurah() = coroutineScope.launch { mediaController.seekToPreviousMediaItem() }

  fun skipToNextSurah() = coroutineScope.launch { mediaController.seekToNextMediaItem() }

  fun playOrPause() {
    coroutineScope.launch {
      if (mediaController.isPlaying) {
        pause()
      } else if (mediaController.currentMediaItem != null) {
        val mediaId = MediaId.fromString(mediaController.currentMediaItem!!.mediaId)
        val reciterId = audioSettings.getCurrentReciter().first()

        if (mediaController.currentMediaItem != null && mediaId.reciter == reciterId) {
          mediaController.play()
        } else {
          val position = audioSettings.getPlaybackHistory().first()
          onPlaySurah(
            sura = position.surah, reciterId = reciterId, startAya = position.ayah
          )
        }
      }
    }
  }

  fun setRepeatMode() {
    coroutineScope.launch {
      mediaController.repeatMode = when (mediaController.repeatMode) {
        Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ONE
        Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_ALL
        else -> Player.REPEAT_MODE_OFF
      }
    }
  }

  fun seekToPosition(position: Long) = coroutineScope.launch { mediaController.seekTo(position) }
}