package org.quran.datastore.serializers

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import org.quran.datastore.AudioPreferences
import org.quran.datastore.RecentPlayback
import java.io.InputStream
import java.io.OutputStream

const val DEFAULT_RECITER = "mishari_alafasy"

internal class AudioPreferencesSerializer : Serializer<AudioPreferences> {
  override val defaultValue: AudioPreferences = AudioPreferences.newBuilder()
    .setCurrentReciter(DEFAULT_RECITER)
    .setRecentPlayback(
      RecentPlayback.newBuilder().setReciterId(DEFAULT_RECITER).setSurah(1).setAyah(1)
    )
    .build()

  override suspend fun readFrom(input: InputStream): AudioPreferences = try {
    (AudioPreferences.parseFrom(input))
  } catch (exception: InvalidProtocolBufferException) {
    throw CorruptionException("Cannot read proto.", exception)
  }

  override suspend fun writeTo(t: AudioPreferences, output: OutputStream) {
    t.writeTo(output)
  }
}