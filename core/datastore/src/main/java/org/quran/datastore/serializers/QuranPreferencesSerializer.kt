package org.quran.datastore.serializers

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import org.quran.datastore.DisplayMode
import org.quran.datastore.FontScale
import org.quran.datastore.QuranPreferences
import java.io.InputStream
import java.io.OutputStream

val DEFAULT_QURAN_FONT_SIZE = FontScale.NORMAL
val DEFAULT_TRANSLATION_FONT_SIZE = FontScale.NORMAL
val DEFAULT_TRANSLATIONS = listOf("transliteration")
val DEFAULT_TRANSLATION = "transliteration"
const val DEFAULT_QURAN_FONT_VERSION = 1

internal class QuranPreferencesSerializer : Serializer<QuranPreferences> {
  override val defaultValue: QuranPreferences = QuranPreferences.newBuilder()
    .setDisplayMode(DisplayMode.QURAN)
    .setFontVersion(DEFAULT_QURAN_FONT_VERSION)
    .setQuranFontScale(FontScale.NORMAL)
    .setTranslationFontScale(FontScale.NORMAL)
    .build()

  override suspend fun readFrom(input: InputStream): QuranPreferences = try {
    QuranPreferences.parseFrom(input)
  } catch (exception: InvalidProtocolBufferException) {
    throw CorruptionException("Cannot read proto.", exception)
  }

  override suspend fun writeTo(t: QuranPreferences, output: OutputStream) {
    t.writeTo(output)
  }
}