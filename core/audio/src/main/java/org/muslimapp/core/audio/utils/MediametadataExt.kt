package org.muslimapp.core.audio.utils

import android.content.Context
import android.net.Uri
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import arg.quran.models.audio.Qari
import arg.quran.models.quran.VerseKey
import org.muslimapp.core.audio.models.MediaId
import org.quram.common.extensions.toUri

const val AYAH_AYAH = "ayah"

fun buildMediaItem(
  context: Context,
  qari: Qari,
  surahName: String,
  surah: Int,
  ayah: Int = 1,
): MediaItem {

  val suraStr = surah.toString().padStart(3, '0')
  val mediaUri = Uri.parse("${qari.url}/$suraStr.mp3")

  val name = context.getString(qari.nameResource)
  val mediaId = MediaId(reciter = qari.slug, sura = surah, ayah = 0)

  val metadata = MediaMetadata.Builder()
    .setTitle(surahName)
    .setArtist(name)
    .setArtworkUri(qari.image.toUri())
    .setTrackNumber(surah)
    .setSubtitle(name)
    .setIsPlayable(true)
    .setAyah(VerseKey(surah, ayah))
    .build()

  val requestMetadata = MediaItem.RequestMetadata.Builder()
    .setMediaUri(mediaUri)
    .build()

  return MediaItem.Builder()
    .setMediaId(mediaId.toString())
    .setUri(mediaUri)
    .setMediaMetadata(metadata)
    .setRequestMetadata(requestMetadata)
    .build()
}

fun MediaMetadata.Builder.setAyah(ayah: VerseKey): MediaMetadata.Builder {
  return setExtras(bundleOf(AYAH_AYAH to ayah.toString()))
}

val MediaMetadata.aya get() = extras?.getString(AYAH_AYAH)?.let { VerseKey.fromString(it) }

