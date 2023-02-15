package org.alquran.audio.models

import android.net.Uri
import androidx.media3.common.C
import java.util.concurrent.TimeUnit

data class NowPlaying(
    val title: String,
    val reciterName: String,
    val artWork: Uri?,
    val sura: Int,
    val ayah: Int,
    val reciter: String,
    val position: Long = C.TIME_UNSET,
    val positionStr: String = position.toTime(),
    val bufferedPosition: Long = C.TIME_UNSET,
    val duration: Long = C.TIME_UNSET,
    val durationStr: String = duration.toTime(),
    val state: AudioState
)

val NowPlaying.progress: Float get() = if (position < 0) 0f else position.toFloat() / duration


fun Long.toTime(): String {
    val hours = TimeUnit.MILLISECONDS.toHours(this)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(this) -TimeUnit.HOURS.toMinutes(hours)

    val seconds = TimeUnit.MILLISECONDS.toSeconds(this) -
            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this))
    return String.format("%d:%02d:%02d", hours, minutes, seconds).removePrefix("0:")
}