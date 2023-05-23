package org.quran.translation.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.quran.datastore.LanguageDirection
import org.quran.datastore.LocaleTranslation
import org.quran.datastore.TranslationList
import org.quran.datastore.copy
import org.quran.datastore.localeTranslation
import org.quran.translation.api.TranslationApiService
import org.quran.translation.api.model.fallbackSlug

internal class TranslationsRepositoryImpl(
  private val context: Context,
  private val apiService: TranslationApiService,
  private val translationDataStore: DataStore<TranslationList>,
) : TranslationsRepository {

  override fun observeAvailableTranslations(): Flow<List<LocaleTranslation>> {
    return translationDataStore.data.map { it.localesList }.map { translations ->
      if (translations.isEmpty()) downloadTranslations()
      translations
    }.flowOn(Dispatchers.IO)
  }

  override fun observeSelectedTranslationSlugs(): Flow<List<String>> {
    return translationDataStore.data.map { it.selectedTranslationsList }
  }

  override suspend fun getTranslation(slug: String): LocaleTranslation? {
    return translationDataStore.data.map { data ->
      data.localesList.find { translation -> translation.slug == slug }
    }.first()
  }

  override suspend fun getSelectedTranslationSlugs(): List<String> {
    return observeSelectedTranslationSlugs().first()
  }

  override suspend fun enableTranslation(slug: String) {
    translationDataStore.updateData {
      val builder = it.toBuilder()
      val editions = builder.selectedTranslationsList.toMutableSet()
      editions.add(slug)

      builder
        .clearSelectedTranslations()
        .addAllSelectedTranslations(editions)
        .build()
    }
  }

  override suspend fun downloadTranslation(slug: String) {
    translationDataStore.updateData {
      val builder = it.toBuilder()
      val translations = it.localesList.map { translation ->
        if (translation.slug == slug) translation.copy { downloaded = true } else translation
      }
      builder
        .clearLocales()
        .addAllLocales(translations)
        .build()
    }
  }

  override suspend fun disableTranslation(slug: String) {
    translationDataStore.updateData { data ->
      val builder = data.toBuilder()
      val editions = builder.selectedTranslationsList.filter { it != slug }
      builder.clearSelectedTranslations()
        .addAllSelectedTranslations(editions)
      builder.build()
    }
  }

  override suspend fun moveTranslationDown(slug: String) {
    translationDataStore.updateData { data ->
      val editions = data.selectedTranslationsList.toMutableList()
      val index = editions.indexOf(slug)
      if (index < editions.size - 1) {
        editions.removeAt(index)
        editions.add(index + 1, slug)
      }

      data.toBuilder()
        .clearSelectedTranslations()
        .addAllSelectedTranslations(editions)
        .build()
    }
  }

  override suspend fun moveTranslationUp(slug: String) {
    translationDataStore.updateData { data ->
      val editions = data.selectedTranslationsList.toMutableList()
      val index = editions.indexOf(slug)
      if (index > 0) {
        editions.removeAt(index)
        editions.add(index - 1, slug)
      }

      data.toBuilder()
        .clearSelectedTranslations()
        .addAllSelectedTranslations(editions)
        .build()
    }
  }

  override suspend fun downloadTranslations() {
    val languages = apiService.getAvailableLanguages().languages
    val translations = apiService.getAvailableTranslations()
      .translations
      .mapNotNull { t ->
        val language = languages.find { l -> l.name.lowercase() == t.languageName.lowercase() }
        if (language == null) {
          null
        } else {
          localeTranslation {
            id = t.id
            name = t.name
            authorName = t.authorName
            languageCode = language.isoCode
            slug = t.slug?.ifEmpty { t.fallbackSlug } ?: t.fallbackSlug
            direction = when (language.direction) {
              "ltr" -> LanguageDirection.LTR
              else -> LanguageDirection.RTL
            }
          }
        }
      }
    translationDataStore.updateData { data ->
      data.toBuilder()
        .clearLocales()
        .addAllLocales(translations)
        .build()
    }
  }

  override suspend fun deleteTranslation(slug: String): Boolean {
    return withContext(Dispatchers.IO) {
      translationDataStore.updateData { data ->
        val selectedTranslations = data.selectedTranslationsList.filter { it != slug }
        val translations = data.localesList.map { translation ->
          if (translation.slug == slug) translation.copy { downloaded = false } else translation
        }

        data.toBuilder()
          .clearLocales()
          .addAllLocales(translations)
          .clearSelectedTranslations()
          .addAllSelectedTranslations(selectedTranslations)
          .build()
      }
      context.deleteDatabase(slug)
    }
  }

  override fun observeSelectedEditions(): Flow<List<LocaleTranslation>> {
    return translationDataStore.data.map { preferences ->
      preferences.localesList.filter { it.slug in preferences.selectedTranslationsList }
        .sortedBy { preferences.selectedTranslationsList.indexOf(it.slug) }
    }
  }
}