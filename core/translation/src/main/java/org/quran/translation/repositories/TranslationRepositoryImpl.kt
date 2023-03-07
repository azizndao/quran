package org.quran.translation.repositories

import android.content.Context
import androidx.work.Operation
import arg.quran.models.Direction
import arg.quran.models.quran.VerseKey
import arg.quran.models.quran.VerseTranslation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import org.quran.datastore.LanguageDirection
import org.quran.datastore.TranslationEdition
import org.quran.datastore.repositories.QuranPreferencesRepository
import org.quran.datastore.serializers.DEFAULT_TRANSLATION
import org.quran.datastore.translationEdition
import org.quran.network.translation.TranslationApiService
import org.quran.network.translation.models.fallbackSlug
import org.quran.translation.databases.QuranTranslationsDatabase.Companion.getTranslationDatabase
import org.quran.translation.workers.DownloadTranslationWorker

internal class TranslationRepositoryImpl(
  private val translationApiService: TranslationApiService,
  private val context: Context,
  private val quranPreferences: QuranPreferencesRepository,
) : TranslationRepository {

  override fun getAvailableTranslations(): Flow<List<TranslationEdition>> {
    return quranPreferences.getAvailableTranslations().map { translations ->
      if (translations.isEmpty()) downloadTranslations()
      translations
    }.flowOn(Dispatchers.IO)
  }

  override suspend fun getTranslation(slug: String): TranslationEdition? =
    quranPreferences.getTranslationEdition(slug)

  override suspend fun downloadTranslations() {
    val translations = translationApiService.getAvailableTranslations()
    quranPreferences.setAvailableTranslations(translations.map { translation ->
      translationEdition {
        id = translation.id
        slug = translation.slug ?: translation.fallbackSlug
        authorName = translation.authorName
        languageCode = translation.language
        downloaded = false
        name = translation.name
        order = if (translation.slug == DEFAULT_TRANSLATION) 0 else -1
        direction = when {
          translation.direction === Direction.RTL -> LanguageDirection.RTL
          else -> LanguageDirection.LTR
        }
      }
    })
  }

  override suspend fun getAyahTranslation(key: VerseKey): VerseTranslation? {
    val translation = quranPreferences.getSelectedTranslations().first()
    return getAyahTranslation(key, translation.first().slug)
  }

  override suspend fun getAyahTranslation(key: VerseKey, slug: String): VerseTranslation? {
    if (slug.isEmpty()) return null
    return context.getTranslationDatabase(slug)
      .ayahDao.getAyah(key)
  }

  override suspend fun downloadQuranTranslation(translation: TranslationEdition): Operation {
    return DownloadTranslationWorker.enqueue(context, translation.slug)
  }

  override fun getSelectedTranslations(): Flow<List<TranslationEdition>> {
    return quranPreferences.getSelectedTranslations().onEach { editions ->
      editions.ifEmpty {
        downloadTranslations()
      }
    }
  }

  override fun getQuranTranslations(
    slug: String,
    page: Int,
  ): Flow<List<VerseTranslation>> = context.getTranslationDatabase(slug).ayahDao.getByPage(page)

  override fun getQuranTranslations(
    editions: List<TranslationEdition>,
    page: Int,
  ): Flow<List<TranslationAndVerses>> {
    return combine(editions.map { getQuranTranslations(it, page) }) {
      it.mapIndexed { index, verses -> TranslationAndVerses(editions[index], verses) }
    }
  }

  override fun getQuranTranslations(
    edition: TranslationEdition,
    page: Int,
  ): Flow<List<VerseTranslation>> = context.getTranslationDatabase(edition.slug)
    .ayahDao
    .getByPage(page)
    .onEach { it.ifEmpty { downloadQuranTranslation(edition) } }

  override suspend fun getQuranTranslation(
    translation: TranslationEdition,
    key: VerseKey
  ): VerseTranslation? = context.getTranslationDatabase(translation.slug)
    .ayahDao.getAyah(key)
}

data class TranslationAndVerses(
  val edition: TranslationEdition,
  val verses: List<VerseTranslation>
)