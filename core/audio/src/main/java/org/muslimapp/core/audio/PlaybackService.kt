package org.muslimapp.core.audio

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.widget.Toast
import androidx.core.net.toUri
import androidx.media3.common.*
import androidx.media3.common.AudioAttributes.Builder
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
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.android.ext.android.inject
import org.muslimapp.core.audio.models.MediaId
import org.muslimapp.core.audio.repositories.QariRepository
import org.muslimapp.core.audio.repositories.RecitationRepository
import org.quram.common.utils.MuslimsConstants.MUSLIMS_BROWSABLE_ROOT
import java.io.File
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class PlaybackService : MediaLibraryService() {

  private val serviceJob = SupervisorJob()
  private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

  private val playerListener = PlayerEventListener()

  private lateinit var player: ExoPlayer
  private lateinit var mediaSession: MediaLibrarySession

  private val qariRepository by inject<QariRepository>()
  private val recitationRepository by inject<RecitationRepository>()
  private val httpDataSourceFactory by inject<HttpDataSource.Factory>()
  private val cache by inject<Cache>()


  @SuppressLint("UnsafeOptInUsageError")
  override fun onCreate() {
    super.onCreate()

    val qaris = qariRepository.getQariItemList()

    val dir = File(getExternalFilesDir(null), "qari.json")

    dir.writeText(Json.encodeToString(qaris))

    val audioAttributes = Builder()
      .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
      .setUsage(C.USAGE_MEDIA).build()

    val upstreamFactory = DefaultDataSource.Factory(
      this, httpDataSourceFactory
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
      .setHandleAudioBecomingNoisy(true).build().also { it.addListener(playerListener) }

    val pendingIntent = packageManager?.getLaunchIntentForPackage(packageName)?.let { intent ->
      PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    mediaSession = MediaLibrarySession.Builder(this, player, MediaSessionCallback())
      .also { pendingIntent?.let { intent -> it.setSessionActivity(intent) } }.build()
  }

  override fun onDestroy() {
    serviceJob.cancel()
    player.release()
    mediaSession.release()
    super.onDestroy()
  }

  override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaSession

  inner class MediaSessionCallback : MediaLibrarySession.Callback {
    override fun onGetLibraryRoot(
      session: MediaLibrarySession,
      browser: MediaSession.ControllerInfo,
      params: LibraryParams?
    ): ListenableFuture<LibraryResult<MediaItem>> {
      val mediaMetadata = MediaMetadata.Builder().setIsPlayable(false)
        .setFolderType(MediaMetadata.FOLDER_TYPE_ARTISTS).build()
      val mediaItem = MediaItem.Builder().setMediaId(MUSLIMS_BROWSABLE_ROOT)
        .setMediaMetadata(mediaMetadata).build()
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
      val qaris = qariRepository.getQariList()
      val mediaItem = when {
        parentId == MUSLIMS_BROWSABLE_ROOT -> {
          qaris.map { reciter ->
            val name = getString(reciter.nameResource)
            val metadata = MediaMetadata.Builder()
              .setTitle(name)
              .setArtist(name)
              .setArtworkUri(reciter.image?.toUri())
              .setIsPlayable(false)
              .setFolderType(MediaMetadata.FOLDER_TYPE_TITLES)
              .build()
            MediaItem.Builder().setMediaId(reciter.slug).setMediaMetadata(metadata)
              .build()
          }
        }

        qaris.any { it.slug == parentId } -> recitationRepository.getRecitations(parentId)

        else -> emptyList()
      }
      LibraryResult.ofItemList(mediaItem, params)
    }, Executors.newSingleThreadExecutor())

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

  private inner class PlayerEventListener : Player.Listener {

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