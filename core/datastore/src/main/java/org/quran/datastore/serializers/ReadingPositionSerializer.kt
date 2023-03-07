package org.quran.datastore.serializers

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import org.quran.datastore.ReadingPositionPreferences
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
class ReadingPositionSerializer : Serializer<ReadingPositionPreferences> {
  override val defaultValue: ReadingPositionPreferences = ReadingPositionPreferences
    .getDefaultInstance()

  override suspend fun readFrom(input: InputStream): ReadingPositionPreferences = try {
    (ReadingPositionPreferences.parseFrom(input))
  } catch (exception: InvalidProtocolBufferException) {
    throw CorruptionException("Cannot read proto.", exception)
  }

  override suspend fun writeTo(t: ReadingPositionPreferences, output: OutputStream) {
    t.writeTo(output)
  }
}