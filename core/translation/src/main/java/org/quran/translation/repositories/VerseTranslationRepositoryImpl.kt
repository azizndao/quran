package org.quran.translation.repositories

import android.content.Context
import arg.quran.models.quran.Verse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.quran.datastore.LocaleTranslation
import org.quran.translation.api.TranslationApiService
import org.quran.translation.local.QuranTranslationsDatabase.Companion.getTranslationDatabase
import org.quran.translation.workers.DownloadTranslationWorker

internal class VerseTranslationRepositoryImpl(
  private val context: Context,
  private val apiService: TranslationApiService,
  private val editionRepository: TranslationsRepository,
) : VerseTranslationRepository {

  override suspend fun getAyahTranslation(sura: Int, ayah: Int): Verse? {
    val translation = editionRepository.getSelectedTranslationSlugs().first()
    return getAyahTranslation(sura, ayah, translation)
  }

  override suspend fun getAyahTranslation(sura: Int, ayah: Int, slug: String): Verse? {
    if (slug.isEmpty()) return null
    return context.getTranslationDatabase(slug)
      .ayahDao.getAyah(sura, ayah)
  }

  override suspend fun search(translationId: String, query: String): List<Verse> {
    return withContext(Dispatchers.IO) {
      context
        .getTranslationDatabase(translationId)
        .ayahDao
        .search(query)
    }
  }

  override suspend fun downloadTranslations(translation: LocaleTranslation) {
    withContext(Dispatchers.IO) {
      try {
        val translations = apiService.getAyahTranslations(translation.id).translations
        context.getTranslationDatabase(translation.slug)
          .ayahDao
          .clearAndInsertAll(translations)
        editionRepository.downloadTranslation(translation.slug)
        editionRepository.enableTranslation(translation.slug)
      } catch (e: Exception) {
        editionRepository.deleteTranslation(translation.slug)
        throw e
      }
    }
  }

  override suspend fun enqueueDownload(slug: String) {
    DownloadTranslationWorker.enqueue(context, slug)
  }

  override suspend fun getQuranTranslation(
    translation: LocaleTranslation,
    sura: Int,
    ayah: Int
  ) = context.getTranslationDatabase(translation.slug).ayahDao.getAyah(sura, ayah)

  override fun getQuranTranslations(
    translation: LocaleTranslation,
    limit: Int,
    offset: Int
  ) = context.getTranslationDatabase(translation.slug)
    .ayahDao.observeAllVerses(limit, offset)

  override suspend fun downloadTranslations(translation: String) {
    downloadTranslations(editionRepository.getTranslation(translation)!!)
  }


  override fun getAll(slug: String) = context.getTranslationDatabase(slug)
    .ayahDao.observeAll()

  override fun observeAllVerse(slug: String): Flow<List<Verse>> {
    return context.getTranslationDatabase(slug)
      .ayahDao.observeAllVerses()
  }
}