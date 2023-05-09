package org.quran.datastore.repositories

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.quran.datastore.DisplayMode
import org.quran.datastore.FontScale
import org.quran.datastore.LocaleTranslation
import org.quran.datastore.QuranPreferences
import org.quran.datastore.TranslationList

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

  suspend fun getLocaleTranslation(slug: String) = translationDataStore.data.map { preferences ->
    preferences.localesList.find { it.slug == slug }
  }.first()

  suspend fun enableTranslation(slug: String) {
    translationDataStore.updateData {
      val builder = it.toBuilder()
      val editions = builder.selectedTranslationsList + slug

      builder
        .clearSelectedTranslations()
        .addAllSelectedTranslations(editions)
        .build()
    }
  }

  suspend fun disableTranslation(slug: String) {
    translationDataStore.updateData {
      val builder = it.toBuilder()
      val editions = builder.selectedTranslationsList.filter { it != slug }
      builder.clearSelectedTranslations()
        .addAllSelectedTranslations(editions)
      builder.build()
    }
  }

  fun getAvailableTranslations() = translationDataStore.data.map { it.localesList }

  suspend fun downloadTranslation(slug: String) {
    translationDataStore.updateData { preferences ->
      val translation = preferences.localesList.map { translation ->
        if (translation.slug == slug) {
          enableTranslation(translation.slug)
          translation.toBuilder().setDownloaded(true).build()
        } else {
          translation
        }
      }
      preferences.toBuilder()
        .clearLocales()
        .addAllLocales(translation)
        .build()
    }
  }

  suspend fun deleteTranslation(slug: String) {
    translationDataStore.updateData { preferences ->
      val translation = preferences.localesList.filter {
        val translation = it.slug == slug
        if (translation) disableTranslation(it.slug)
        !translation
      }

      preferences.toBuilder()
        .clearLocales()
        .addAllLocales(translation)
        .build()
    }
  }

  suspend fun setAvailableTranslations(translations: List<LocaleTranslation>) {
    translationDataStore.updateData { preferences ->
      preferences.toBuilder()
        .clearLocales()
        .addAllLocales(translations)
        .build()
    }
  }

  fun getSelectedTranslationSlugs(): Flow<List<String>> {
    return translationDataStore.data.map { it.selectedTranslationsList }
  }

  fun getSelectedTranslations(): Flow<List<LocaleTranslation>> =
    translationDataStore.data.map { pref ->
      pref.localesList.filter { it.slug in pref.selectedTranslationsList }
    }
}