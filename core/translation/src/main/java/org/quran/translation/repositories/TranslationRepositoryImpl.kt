package org.quran.translation.repositories

import android.content.Context
import androidx.work.Operation
import arg.quran.models.Direction
import arg.quran.models.quran.Verse
import arg.quran.models.quran.VerseKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.quran.datastore.LanguageDirection
import org.quran.datastore.LocaleTranslation
import org.quran.datastore.localeTranslation
import org.quran.datastore.repositories.QuranPreferencesRepository
import org.quran.translation.api.TranslationApiService
import org.quran.translation.api.models.fallbackSlug
import org.quran.translation.local.QuranTranslationsDatabase.Companion.getTranslationDatabase
import org.quran.translation.local.models.TranslatedEdition
import org.quran.translation.workers.DownloadTranslationWorker

internal class TranslationRepositoryImpl(
  private val context: Context,
  private val translationApiService: TranslationApiService,
  private val quranPreferences: QuranPreferencesRepository,
) : TranslationRepository {
  override fun getAllVerses(locale: LocaleTranslation): Flow<List<Verse>> {
    return context.getTranslationDatabase(locale.slug).verses.getAyahsFlow()
  }

  override suspend fun search(query: String): List<TranslatedEdition> {
    val translations = getSelectedTranslations().first()
    return translations.map { locale ->
      TranslatedEdition(locale, search(locale, query))
    }
  }

  override suspend fun search(locale: LocaleTranslation, query: String): List<Verse> {
    return context.getTranslationDatabase(locale.slug).verses.search(query)
  }

  override fun getAvailableTranslations(): Flow<List<LocaleTranslation>> {
    return quranPreferences.getAvailableTranslations().map { translations ->
      if (translations.isEmpty()) downloadTranslations()
      translations
    }.flowOn(Dispatchers.IO)
  }

  override suspend fun getTranslation(slug: String): LocaleTranslation? =
    quranPreferences.getLocaleTranslation(slug)


  override suspend fun downloadTranslations() {
    val languages = translationApiService.getAvailableLanguages().languages
    val response = translationApiService.getAvailableTranslations()
    val translations = response.translations.mapNotNull { translation ->
      when (val language =
        languages.find { l -> l.name.lowercase() == translation.languageName.lowercase() }) {
        null -> null
        else -> localeTranslation {
          id = translation.id
          name = translation.name
          authorName = translation.authorName
          languageCode = language.isoCode
          slug = translation.slug?.ifEmpty { translation.fallbackSlug } ?: translation.fallbackSlug
          direction = when (language.direction) {
            Direction.LTR -> LanguageDirection.LTR
            else -> LanguageDirection.RTL
          }
        }
      }
    }
    quranPreferences.setAvailableTranslations(translations)
  }

  override suspend fun getAyahTranslation(key: VerseKey): Verse? {
    val translation = quranPreferences.getSelectedTranslations().first()
    return getAyahTranslation(key, translation.first().slug)
  }

  override suspend fun getAyahTranslation(key: VerseKey, slug: String): Verse? {
    if (slug.isEmpty()) return null
    return context.getTranslationDatabase(slug).verses.getAyah(key.sura, key.aya)
  }

  override suspend fun downloadQuranTranslation(slug: String): Operation {
//    val translation = getTranslation(slug) ?: return
//    val response = translationApiService.getVerses(translation.id)
//    val apiTranslations = response.translations.map { verse ->
//      TranslatedVerse(sura = verse.sura, ayah = verse.ayah, text = verse.text)
//    }
//
//    context.getTranslationDatabase(slug).verses.insertAyahs(apiTranslations)
//    quranPreferences.downloadTranslation(slug)
    return DownloadTranslationWorker.enqueue(context, slug)
  }

  override fun getSelectedTranslationSlugs() = quranPreferences.getSelectedTranslationSlugs()

  override fun getSelectedTranslations(): Flow<List<LocaleTranslation>> {
    return quranPreferences.getSelectedTranslations()
  }

  override suspend fun getQuranTranslation(
    translation: LocaleTranslation,
    key: VerseKey
  ): Verse? = context.getTranslationDatabase(translation.slug)
    .verses
    .getAyah(key.sura, key.aya)
}
