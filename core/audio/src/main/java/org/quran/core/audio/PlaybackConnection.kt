package org.quran.core.audio

import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.work.DirectExecutor
import arg.quran.models.audio.Qari
import arg.quran.models.quran.VerseKey
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.muslimapp.core.audio.models.AyahTiming
import org.muslimapp.core.audio.models.MediaId
import org.quran.core.audio.models.AudioState
import org.quran.core.audio.models.NowPlaying
import org.quran.core.audio.repositories.RecitationRepository
import org.quran.core.audio.repositories.TimingRepository
import org.quran.datastore.repositories.AudioPreferencesRepository

class PlaybackConnection(
  private val context: Context,
  private val recitationRepository: RecitationRepository,
  private val audioSettings: AudioPreferencesRepository,
  private val timingRepository: TimingRepository,
) : DefaultLifecycleObserver {

  private lateinit var coroutineScope: CoroutineScope

  private val _isConnected = MutableStateFlow(false)
  val isConnected = _isConnected.asStateFlow()

  private val _playingState = MutableStateFlow(AudioState())

  private val _repeatMode = MutableStateFlow(Player.REPEAT_MODE_OFF)
  val repeatMode = _repeatMode.asStateFlow()

  private val _currentMediaItem = MutableStateFlow<MediaItem?>(null)
  val currentMediaItem: StateFlow<MediaItem?> get() = _currentMediaItem


  val currentAyah by lazy {
    nowPlaying.map { playing -> playing?.let { VerseKey(it.sura, it.ayah) } }.distinctUntilChanged()
  }

  val nowPlaying by lazy {
    getTimingFlow(100).combine(_playingState) { timing, state ->
      val currentMediaItem = mediaController.currentMediaItem

      if (currentMediaItem == null || timing == null) return@combine null

      val mediaMetadata = currentMediaItem.mediaMetadata
      val mediaId = MediaId.fromString(currentMediaItem.mediaId)

      NowPlaying(
        title = mediaMetadata.title.toString() + " (${mediaId.sura}:${timing.ayah})",
        reciterName = mediaMetadata.artist.toString(),
        sura = timing.sura,
        ayah = timing.ayah,
        reciter = mediaId.reciter,
        artWork = mediaMetadata.artworkUri,
        position = mediaController.currentPosition,
        bufferedPosition = mediaController.bufferedPosition,
        duration = mediaController.duration,
        isPlaying = state.isPlaying,
        isLoading = state.isLoading,
      )
    }.stateIn(coroutineScope, SharingStarted.Eagerly, null)
  }

  private var mediaControllerFuture: ListenableFuture<MediaController>? = null

  private lateinit var mediaController: MediaController

  override fun onStart(owner: LifecycleOwner) {
    connect()
  }

  override fun onStop(owner: LifecycleOwner) {
    release()
  }

  private fun connect() {
    coroutineScope = CoroutineScope(Dispatchers.Main + Job())
    mediaControllerFuture = MediaController.Builder(
      context, SessionToken(context, ComponentName(context, PlaybackService::class.java))
    ).buildAsync()
    mediaControllerFuture?.addListener({
      if (mediaControllerFuture!!.isDone) {
        mediaController = mediaControllerFuture!!.get()
        mediaController.addListener(PlayerListener())
        _isConnected.value = true
        _currentMediaItem.value = mediaController.currentMediaItem
      }
    }, DirectExecutor.INSTANCE)
  }

  private fun release() {
    coroutineScope.cancel()
    mediaControllerFuture?.let { MediaController.releaseFuture(it) }
  }

  private fun getTimingFlow(delay: Long = 200): Flow<AyahTiming?> =
    _currentMediaItem.flatMapLatest { mediaItem ->
      flow {
        if (mediaItem == null) {
          return@flow emit(null)
        }

        while (true) {
          val mediaId = MediaId.fromString(mediaItem.mediaId)

          val timing = timingRepository.getTiming(
            mediaId.reciter,
            mediaId.sura,
            withContext(Dispatchers.Main) { mediaController.currentPosition })

          emit(timing)

          delay(delay)
        }
      }
    }.flowOn(Dispatchers.IO)

  inner class PlayerListener : Player.Listener {

    override fun onEvents(player: Player, events: Player.Events) {
      if (events.contains(Player.EVENT_REPEAT_MODE_CHANGED)) {
        _repeatMode.value = player.repeatMode
      }

      _playingState.value = AudioState(player.isLoading, player.isPlaying)
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
      _currentMediaItem.value = mediaItem
    }
  }

  fun onPlaySurah(suraAyah: VerseKey) {
    coroutineScope.launch {
      val reciter = audioSettings.getCurrentReciter().first()
      onPlaySurah(sura = suraAyah.sura, reciterId = reciter, startAya = suraAyah.aya)
    }
  }

  fun onPlaySurah(sura: Int, ayah: Int) {
    coroutineScope.launch {
      val reciter = audioSettings.getCurrentReciter().first()
      onPlaySurah(sura, reciter, ayah)
    }
  }

  fun onPlaySurah(sura: Int, reciterId: String, startAya: Int = 0) {
    coroutineScope.launch(Dispatchers.Main) {
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
    withContext(Dispatchers.IO) {
      val mediaItems = recitationRepository.getRecitations(reciter.path)
      val position = timingRepository.getPosition(reciter.path, surah, startAyah)
      prepareAndPlay(mediaItems, surah, position)
    }
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

  fun repeatAyah(verse: VerseKey) {

  }
}