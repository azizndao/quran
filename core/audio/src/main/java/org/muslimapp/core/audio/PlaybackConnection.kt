package org.muslimapp.core.audio

import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import arg.quran.models.audio.Qari
import arg.quran.models.quran.VerseKey
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.alquran.audio.models.NowPlaying
import org.muslimapp.core.audio.models.MediaId
import org.muslimapp.core.audio.repositories.RecitationRepository
import org.muslimapp.core.audio.repositories.TimingRepository
import org.muslimapp.core.audio.utils.qari
import org.quram.common.core.QuranInfo
import org.quram.common.utils.QuranDisplayData
import org.quran.datastore.repositories.AudioPreferencesRepository
import java.util.concurrent.Executors
import kotlin.math.abs

class PlaybackConnection(
  private val context: Context,
  private val recitationRepository: RecitationRepository,
  private val audioSettings: AudioPreferencesRepository,
  private val timingRepository: TimingRepository,
  private val quranInfo: QuranInfo,
  private val quranDisplayData: QuranDisplayData,
) : DefaultLifecycleObserver {

  private val job = SupervisorJob()
  private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

  private val _isConnected = MutableStateFlow(false)
//  val isConnected = _isConnected.asStateFlow()

  private val _repeatMode = MutableStateFlow(Player.REPEAT_MODE_OFF)
  val repeatMode = _repeatMode.asStateFlow()

  private val _currentMediaItem = MutableStateFlow<MediaItem?>(null)
  val currentMediaItem: StateFlow<MediaItem?> get() = _currentMediaItem

  private var gaplessSura = currentMediaItem
    .map { it?.let { MediaId.fromString(it.mediaId) } }
    .distinctUntilChanged()
    .stateIn(coroutineScope, SharingStarted.WhileSubscribed(), null)

  private var gaplessSuraData =
    gaplessSura.map { it?.let { timingRepository.getGaplessData(it.reciter, it.sura) } }


  val playingAyahFlow = gaplessSuraData.transform { data ->
    data ?: return@transform emit(null)
    while (true) {
      val (_, sura, ayah) = gaplessSura.value ?: return@transform emit(null)

      var updatedAyah = ayah
      val pos = withContext(Dispatchers.Main) { mediaController.currentPosition }
      var ayahTime = data[ayah]
      val maxAyahs = quranInfo.getNumberOfAyahs(sura)
      var iterAyah = ayah
      if (ayahTime > pos) {
        while (--iterAyah > 0) {
          ayahTime = data[iterAyah]
          if (ayahTime <= pos) {
            updatedAyah = iterAyah
            break
          } else {
            updatedAyah--
          }
        }
      } else {
        while (++iterAyah <= maxAyahs) {
          ayahTime = data[iterAyah]
          if (ayahTime > pos) {
            updatedAyah = iterAyah - 1
            break
          } else {
            updatedAyah++
          }
        }
      }
      if (updatedAyah != ayah) emit(VerseKey(sura, updatedAyah))
      ayahTime = if (updatedAyah < quranInfo.getNumberOfAyahs(sura) - 1) {
        data[updatedAyah + 1]
      } else {
        0
      }
      delay(abs(pos - ayahTime))
    }
  }.distinctUntilChanged()
    .stateIn(coroutineScope, SharingStarted.Lazily, null)


  val nowPlaying = playingAyahFlow.combineTransform(currentMediaItem) { key, mediaItem ->
    if (key == null || mediaItem == null) return@combineTransform emit(null)

    while (true) {
      val playing = NowPlaying(
        title = quranDisplayData.getSuraAyahString(key.sura, key.aya),
        reciterName = mediaItem.mediaMetadata.artist.toString(),
        sura = key.sura,
        ayah = key.aya,
        reciter = mediaItem.mediaMetadata.qari ?: "",
        artWork = mediaItem.mediaMetadata.artworkUri,
        position = mediaController.currentPosition,
        bufferedPosition = mediaController.bufferedPosition,
        duration = mediaController.duration,
        isPlaying = withContext(Dispatchers.Main) { mediaController.isPlaying },
        isLoading = withContext(Dispatchers.Main) { mediaController.isLoading }
      )
      emit(playing)
      delay(150)
    }
  }.stateIn(coroutineScope, SharingStarted.Lazily, null)

  private var mediaControllerFuture: ListenableFuture<MediaController>? = null

  private lateinit var mediaController: MediaController


  override fun onStart(owner: LifecycleOwner) {
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

  override fun onStop(owner: LifecycleOwner) {
    coroutineScope.cancel()
    mediaControllerFuture?.let { MediaController.releaseFuture(it) }
  }

  inner class PlayerListener : Player.Listener {

    override fun onEvents(player: Player, events: Player.Events) {
      if (events.contains(Player.EVENT_REPEAT_MODE_CHANGED)) {
        _repeatMode.value = player.repeatMode
      }
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
      _currentMediaItem.value = mediaItem
    }
  }

  fun onPlaySurah(key: VerseKey) = onPlaySurah(key.sura, key.aya)

  fun repeatAyah(key: VerseKey) = onPlaySurah(key.sura, key.aya)

  private fun onPlaySurah(sura: Int, ayah: Int) {
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
}