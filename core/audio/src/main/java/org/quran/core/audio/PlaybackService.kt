package org.quran.core.audio

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.widget.Toast
import androidx.core.net.toUri
import androidx.media3.common.AudioAttributes.Builder
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.HttpDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSink
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.quran.core.audio.PlaybackConstants.MUSLIMS_BROWSABLE_ROOT
import org.quran.core.audio.models.MediaId
import org.quran.core.audio.repositories.QariRepository
import org.quran.core.audio.repositories.TimingRepository
import org.quran.core.audio.repositories.RecitationRepository
import org.quran.datastore.repositories.AudioPreferencesRepository
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class PlaybackService : MediaLibraryService() {

  private val serviceJob = SupervisorJob()
  private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

  private val playerListener = PlayerEventListener()

  private lateinit var player: ExoPlayer
  private lateinit var mediaSession: MediaLibrarySession

 private val recitationRepository: RecitationRepository by inject()

  private val httpDataSourceFactory: HttpDataSource.Factory by inject()

  private val cache: Cache by inject()

 private val audioPreferences: AudioPreferencesRepository by inject()

  private val timingRepository: TimingRepository by inject()

  private val qariRepository: QariRepository by inject()


  private val currentMediaItem = MutableStateFlow<MediaItem?>(null)

  @SuppressLint("UnsafeOptInUsageError")
  override fun onCreate() {
    super.onCreate()

    val audioAttributes = Builder()
      .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
      .setUsage(C.USAGE_MEDIA)
      .build()

    val upstreamFactory = DefaultDataSource.Factory(
      this,
      httpDataSourceFactory
    )

    val cacheWriteDataSinkFactory = CacheDataSink.Factory()
      .setCache(cache)
      .setFragmentSize(C.LENGTH_UNSET.toLong())

    val cacheDataSource = CacheDataSource.Factory()
      .setCache(cache)
      .setUpstreamDataSourceFactory(upstreamFactory)
      .setCacheWriteDataSinkFactory(cacheWriteDataSinkFactory)
      .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)

    player = ExoPlayer.Builder(this)
      .setAudioAttributes(audioAttributes, true)
      .setMediaSourceFactory(DefaultMediaSourceFactory(cacheDataSource))
      .setHandleAudioBecomingNoisy(true)
      .build().also { it.addListener(playerListener); }

    val pendingIntent = packageManager?.getLaunchIntentForPackage(packageName)?.let { intent ->
      PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    mediaSession = MediaLibrarySession.Builder(this, player, MediaSessionCallback())
      .also { pendingIntent?.let { intent -> it.setSessionActivity(intent) } }
      .build()

    serviceScope.launch {
      val history = audioPreferences.getPlaybackHistory().first()
      val mediaItems = recitationRepository.getRecitations(history.reciterId)
      val position =
        timingRepository.getPosition(history.reciterId, history.surah, history.ayah)

      player.setMediaItems(mediaItems, history.surah - 1, position)
      player.prepare()

      currentMediaItem.value = mediaItems[history.surah - 1]

      currentTiming.filterNotNull().collect { timing ->
        audioPreferences.setRecentPlayback(
          sura = timing.sura,
          aya = timing.ayah,
          reciterId = MediaId.fromString(currentMediaItem.value!!.mediaId).reciter
        )
      }
    }
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  private val currentTiming = currentMediaItem.flatMapLatest { mediaItem ->
    flow {
      while (true) {
        if (mediaItem == null) {
          emit(null)
          break
        }
        val mediaId = MediaId.fromString(mediaItem.mediaId)

        val timing = timingRepository.getTiming(
          mediaId.reciter,
          mediaId.sura,
          withContext(Dispatchers.Main) { player.currentPosition })

        emit(timing)

        delay(1000)
      }
    }.distinctUntilChanged()
  }.flowOn(Dispatchers.IO).stateIn(serviceScope, SharingStarted.Eagerly, null)

  override fun onDestroy() {
    serviceJob.cancel()
    player.release()
    mediaSession.release()
    super.onDestroy()
  }

  inner class MediaSessionCallback : MediaLibrarySession.Callback {

    override fun onConnect(
      session: MediaSession,
      controller: MediaSession.ControllerInfo
    ): MediaSession.ConnectionResult {
      return super.onConnect(session, controller)
    }

    override fun onGetLibraryRoot(
      session: MediaLibrarySession,
      browser: MediaSession.ControllerInfo,
      params: LibraryParams?
    ): ListenableFuture<LibraryResult<MediaItem>> {
      val mediaMetadata = MediaMetadata.Builder()
        .setIsPlayable(false)
        .setFolderType(MediaMetadata.FOLDER_TYPE_ARTISTS)
        .build()
      val mediaItem = MediaItem.Builder().setMediaId(MUSLIMS_BROWSABLE_ROOT)
        .setMediaMetadata(mediaMetadata)
        .build()
      return Futures.immediateFuture(LibraryResult.ofItem(mediaItem, params))
    }

    override fun onGetChildren(
      session: MediaLibrarySession,
      browser: MediaSession.ControllerInfo,
      parentId: String,
      page: Int,
      pageSize: Int,
      params: LibraryParams?
    ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> = Futures.submit(Callable {
      val mediaItem = when {
        parentId == MUSLIMS_BROWSABLE_ROOT -> qariRepository.getQariItemList().map { reciter ->
          val metadata = MediaMetadata.Builder()
            .setTitle(reciter.name)
            .setArtist(reciter.name)
            .setArtworkUri(reciter.imageUri?.toUri())
            .setIsPlayable(false)
            .setFolderType(MediaMetadata.FOLDER_TYPE_TITLES)
            .build()
          MediaItem.Builder()
            .setMediaId(reciter.path)
            .setMediaMetadata(metadata)
            .build()
        }

        qariRepository.getQariList().any { it.path == parentId } -> runBlocking {
          recitationRepository.getRecitations(parentId)
        }

        else -> emptyList()
      }
      LibraryResult.ofItemList(mediaItem, params)
    }, Executors.newSingleThreadExecutor())

    override fun onGetItem(
      session: MediaLibrarySession,
      browser: MediaSession.ControllerInfo,
      mediaId: String
    ): ListenableFuture<LibraryResult<MediaItem>> {
      return super.onGetItem(session, browser, mediaId)
    }

    override fun onSubscribe(
      session: MediaLibrarySession,
      browser: MediaSession.ControllerInfo,
      parentId: String,
      params: LibraryParams?
    ): ListenableFuture<LibraryResult<Void>> {
      session.notifyChildrenChanged(browser, parentId, 114, params)
      return super.onSubscribe(session, browser, parentId, params)
    }

    override fun onAddMediaItems(
      mediaSession: MediaSession,
      controller: MediaSession.ControllerInfo,
      mediaItems: MutableList<MediaItem>,
    ): ListenableFuture<List<MediaItem>> {
      val updatedMediaItems = mediaItems.map { mediaItem ->
        mediaItem.buildUpon()
          .setUri(mediaItem.requestMetadata.mediaUri)
          .setMediaMetadata(mediaItem.mediaMetadata)
          .build()
      }
      return Futures.immediateFuture(updatedMediaItems)
    }
  }

  override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaSession


  private inner class PlayerEventListener : Player.Listener {

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
      serviceScope.launch {
        currentMediaItem.value = mediaItem
        if (mediaItem?.mediaId != null) {
          val mediaId: MediaId = MediaId.fromString(mediaItem.mediaId)
          audioPreferences.setRecentPlayback(mediaId.reciter, mediaId.sura, mediaId.ayah)
        }
      }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
      super.onPlaybackStateChanged(playbackState)
      if (playbackState == Player.STATE_ENDED) serviceScope.launch {
        val metadata = player.getMediaItemAt(player.currentMediaItemIndex)
        val mediaId = MediaId.fromString(metadata.mediaId)
        if (mediaId.sura < 114) {
          recitationRepository.downloadRecitation(mediaId.sura + 1, mediaId.reciter)
        }
      }
    }

    override fun onPlayerError(error: PlaybackException) {
      Toast.makeText(applicationContext, "Error: ${error.message}", Toast.LENGTH_LONG).show()
    }
  }
}