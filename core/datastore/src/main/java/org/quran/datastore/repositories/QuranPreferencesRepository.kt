package org.quran.datastore.repositories

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.quran.datastore.DisplayMode
import org.quran.datastore.FontScale
import org.quran.datastore.QuranPreferences

class QuranPreferencesRepository internal constructor(
  private val dataStore: DataStore<QuranPreferences>,
) {
  fun getAllPreferences(): Flow<QuranPreferences> = dataStore.data

  fun getDisplayMode(): Flow<DisplayMode> = dataStore.data.map { it.displayMode }
    .distinctUntilChanged()

  fun getFontVersion() = dataStore.data.map { it.fontVersion }

  suspend fun setFontVersion(version: Int) = dataStore.updateData {
    it.toBuilder()
      .setFontVersion(version)
      .build()
  }

  fun getQuranScale() = dataStore.data.map { it.quranFontScale }

  suspend fun setQuranScale(scale: FontScale) = dataStore.updateData {
    it.toBuilder()
      .setQuranFontScale(scale)
      .build()
  }

  fun getTranslationScale() = dataStore.data.map { it.translationFontScale }

  suspend fun setTranslationScale(scale: FontScale) = dataStore.updateData {
    it.toBuilder()
      .setTranslationFontScale(scale)
      .build()
  }

  suspend fun setDisplayMode(displayMode: DisplayMode) {
    dataStore.updateData { it.toBuilder().setDisplayMode(displayMode).build() }
  }

  suspend fun setQuranFontScale(scale: FontScale) {
    dataStore.updateData { it.toBuilder().setQuranFontScale(scale).build() }
  }
}