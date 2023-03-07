package org.quran.datastore.repositories

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.quran.datastore.*

class QuranPreferencesRepository internal constructor(
  private val dataStore: DataStore<QuranPreferences>,
  private val translationDataStore: DataStore<TranslationList>,
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

  suspend fun getTranslationEdition(slug: String) = translationDataStore.data.map { preferences ->
    preferences.editionsList.find { it.slug == slug }
  }.first()

  suspend fun enableTranslation(slug: String) {
    translationDataStore.updateData {
      val builder = it.toBuilder()
      var index = 0
      val editions = builder.editionsList.map { edition ->
        if (edition.slug == slug) {
          edition.copy { order = index }
        } else {
          index += 1
          edition
        }
      }

      builder
        .clearEditions()
        .addAllEditions(editions)
        .build()
    }
  }

  suspend fun disableTranslation(slug: String) {
    translationDataStore.updateData {
      val builder = it.toBuilder()
      val editions = builder.editionsList.map { edition ->
        if (edition.slug == slug) edition.copy { order = -1 } else edition
      }
      builder.clearEditions()
        .addAllEditions(editions)
      builder.build()
    }
  }

  fun getAvailableTranslations() = translationDataStore.data.map { it.editionsList }

  suspend fun downloadTranslation(id: Int) {
    translationDataStore.updateData { preferences ->
      val translation = preferences.editionsList.map {
        if (it.id == id) {
          enableTranslation(it.slug)
          it.copy { downloaded = true }
        } else {
          it
        }
      }
      preferences.toBuilder()
        .clearEditions()
        .addAllEditions(translation)
        .build()
    }
  }

  suspend fun deleteTranslation(id: Int) {
    translationDataStore.updateData { preferences ->
      val translation = preferences.editionsList.filter {
        val translation = it.id == id
        if (translation) disableTranslation(it.slug)
        !translation
      }

      preferences.toBuilder()
        .clearEditions()
        .addAllEditions(translation)
        .build()
    }
  }

  suspend fun setAvailableTranslations(translations: List<TranslationEdition>) {
    translationDataStore.updateData { preferences ->
      preferences.toBuilder()
        .clearEditions()
        .addAllEditions(translations)
        .build()
    }
  }

  fun getSelectedTranslations(): Flow<List<TranslationEdition>> =
    translationDataStore.data.map { pref ->
      pref.editionsList.filter { it.order != -1 }
    }
}