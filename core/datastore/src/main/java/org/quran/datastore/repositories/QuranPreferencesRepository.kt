package org.quran.datastore.repositories

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.*
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

    fun getSelectedTranslationSlugs() =
        dataStore.data.map { it.selectedTranslationsList }.distinctUntilChanged()

    suspend fun getTranslationEdition(slug: String) = translationDataStore.data.map { preferences ->
        preferences.editionsList.find { it.slug == slug }
    }.first()

    suspend fun enableTranslation(slug: String) {
        dataStore.updateData {
            val builder = it.toBuilder()
            val translations = builder.selectedTranslationsList
            translations.add(slug)
            builder.addAllSelectedTranslations(translations.toSet())
            builder.build()
        }
    }

    suspend fun disableTranslation(value: String) {
        dataStore.updateData { preferences ->
            val builder = preferences.toBuilder()
            val translations = builder.selectedTranslationsList.filter { it != value }
            builder.addAllSelectedTranslations(translations)
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

    fun getSelectedTranslations(): Flow<List<TranslationEdition>> = combine(
        getAvailableTranslations(),
        getSelectedTranslationSlugs()
    ) { edition, selectedEditionIds ->
        edition.filter { it.slug in selectedEditionIds }
    }

    fun getSelectedTranslationId(): Flow<List<Int>> {
        return getSelectedTranslations().map { it.map { edition -> edition.id } }
    }
}