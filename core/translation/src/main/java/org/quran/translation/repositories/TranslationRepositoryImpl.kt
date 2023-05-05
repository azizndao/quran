package org.quran.translation.repositories

import android.content.Context
import androidx.work.Operation
import arg.quran.models.Direction
import arg.quran.models.quran.Verse
import arg.quran.models.quran.VerseKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.quram.common.core.QuranInfo
import org.quran.datastore.LanguageDirection
import org.quran.datastore.LocaleTranslation
import org.quran.datastore.localeTranslation
import org.quran.datastore.repositories.QuranPreferencesRepository
import org.quran.network.translation.TranslationApiService
import org.quran.network.translation.models.fallbackSlug
import org.quran.translation.databases.QuranTranslationsDatabase.Companion.getTranslationDatabase
import org.quran.translation.models.TranslatedEdition
import org.quran.translation.workers.DownloadTranslationWorker
import timber.log.Timber

internal class TranslationRepositoryImpl(
  private val translationApiService: TranslationApiService,
  private val context: Context,
  private val quranPreferences: QuranPreferencesRepository,
  private val quranInfo: QuranInfo,
) : TranslationRepository {

  private val _caches = quranPreferences.getSelectedTranslations().flatMapMerge { translations ->
    if (translations.isEmpty()) return@flatMapMerge flowOf(emptyList())
    val flowList = translations.sortedBy { it.order }.map { locale ->
      context.getTranslationDatabase(locale.slug).verses.getAyahsFlow()
        .map { verses -> TranslatedEdition(locale, verses) }
    }

    combine(flowList) { editions -> editions.filter { it.verses.isNotEmpty() } }
  }.catch { Timber.e(it) }

  override fun getAvailableTranslations(): Flow<List<LocaleTranslation>> {
    return quranPreferences.getAvailableTranslations().map { translations ->
      if (translations.isEmpty()) downloadTranslations()
      translations
    }.flowOn(Dispatchers.IO)
  }

  override suspend fun getTranslation(slug: String): LocaleTranslation? =
    quranPreferences.getLocaleTranslation(slug)


  override suspend fun downloadTranslations() {
    val languages = translationApiService.getAvailableLanguages()
    val translations = translationApiService
      .getAvailableTranslations()
      .mapNotNull { t ->
        when (val language = languages.find { l -> l.name.lowercase() == t.languageName.lowercase() }) {
          null -> null
          else -> localeTranslation {
            id = t.id
            name = t.name
            authorName = t.authorName
            languageCode = language.isoCode
            slug = t.slug?.ifEmpty { t.fallbackSlug } ?: t.fallbackSlug
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

  override suspend fun downloadQuranTranslation(translation: LocaleTranslation): Operation {
    return DownloadTranslationWorker.enqueue(context, translation.slug)
  }

  override fun getSelectedTranslations(): Flow<List<LocaleTranslation>> {
    return quranPreferences.getSelectedTranslations()
  }

  override fun getVerses(
    slug: String,
    page: Int,
  ): Flow<TranslatedEdition?> = _caches.map { editions ->
    val edition = editions.find { it.locale.slug == slug }
    edition?.copy(verses = edition.verses.filter { true })
  }

  override fun getSelectedEditions(
    page: Int,
  ): Flow<List<TranslatedEdition>> {
    val range = quranInfo.getVerseRangeForPage(page)
    return _caches.map { caches ->
      caches.map { (locale, verses) ->
        val pageVerses = if (range.startSura == range.endingSura) {
          verses
            .filter { verse ->
              verse.sura == range.endingSura &&
                verse.ayah >= range.startAyah &&
                verse.ayah <= range.endingAyah
            }
        } else
          verses.filter { v ->
            v.sura == range.startSura && v.ayah >= range.startAyah ||
              v.sura == range.endingSura && v.ayah <= range.endingAyah ||
              v.sura > range.startSura && v.sura < range.endingSura
          }.take(range.versesInRange)
        TranslatedEdition(locale, pageVerses)
      }
    }
  }

  override fun getVerses(
    edition: LocaleTranslation,
    page: Int,
  ): Flow<TranslatedEdition?> = getVerses(edition.slug, page)

  override suspend fun getQuranTranslation(
    translation: LocaleTranslation,
    key: VerseKey
  ): Verse? = context.getTranslationDatabase(translation.slug)
    .verses
    .getAyah(key.sura, key.aya)
}
