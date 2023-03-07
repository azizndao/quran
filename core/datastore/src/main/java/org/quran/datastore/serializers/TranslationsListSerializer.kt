package org.quran.datastore.serializers

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import org.quran.datastore.TranslationList
import java.io.InputStream
import java.io.OutputStream


class TranslationsListSerializer : Serializer<TranslationList> {
  override val defaultValue: TranslationList = TranslationList.getDefaultInstance()

  override suspend fun readFrom(input: InputStream): TranslationList {
    return try {
      (TranslationList.parseFrom(input))
    } catch (exception: InvalidProtocolBufferException) {
      throw CorruptionException("Cannot read proto.", exception)
    }
  }

  override suspend fun writeTo(t: TranslationList, output: OutputStream) {
    t.writeTo(output)
  }
}
