package org.quran.translation.repositories

import android.content.Context
import androidx.work.Operation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import org.quram.common.model.VerseKey
import org.quran.datastore.LanguageDirection
import org.quran.datastore.TranslationEdition
import org.quran.datastore.TranslationEdition.newBuilder
import org.quran.datastore.repositories.QuranPreferencesRepository
import org.quran.network.model.fallbackSlug
import org.quran.network.services.TranslationApiService
import org.quran.translation.databases.QuranTranslationsDatabase.Companion.getTranslationDatabase
import org.quran.translation.model.VerseTranslation
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
        val languages = translationApiService.getAvailableLanguages()
        val translations = translationApiService
            .getAvailableTranslations()
            .mapNotNull { t ->
                when (val language =
                    languages.find { l -> l.name.lowercase() == t.languageName.lowercase() }) {
                    null -> null
                    else -> {
                        val slug = t.slug?.ifEmpty { t.fallbackSlug } ?: t.fallbackSlug
                        newBuilder()
                            .setId(t.id)
                            .setName(t.name)
                            .setAuthorName(t.authorName)
                            .setLanguageCode(language.isoCode)
                            .setSlug(slug)
                            .setDirection(if (language.direction == "ltr") LanguageDirection.LTR else LanguageDirection.RTL)
                            .build()
                    }
                }
            }
        quranPreferences.setAvailableTranslations(translations)
    }

    override suspend fun getAyahTranslation(key: VerseKey): VerseTranslation? {
        val translation = quranPreferences.getSelectedTranslationSlugs().first()
        return getAyahTranslation(key, translation.first())
    }

    override suspend fun getAyahTranslation(key: VerseKey, slug: String): VerseTranslation? {
        if (slug.isEmpty()) return null
        return context.getTranslationDatabase(slug)
            .ayahDao.getAyah(key)
    }

    override suspend fun downloadQuranTranslation(translation: TranslationEdition): Operation {
        return DownloadTranslationWorker.enqueue(context, translation.slug)
    }

    override fun getQuranTranslations(
        slug: String,
        page: Int,
        version: Int
    ): Flow<List<VerseTranslation>> =
        context.getTranslationDatabase(slug)
            .ayahDao.getByV1Page(page)

    override fun getQuranTranslations(
        editions: List<TranslationEdition>,
        page: Int,
        version: Int,
    ): Flow<Array<List<VerseTranslation>>> =
        combine(editions.map { getQuranTranslations(it, page, version) }) { it }

    override fun getQuranTranslations(
        edition: TranslationEdition,
        page: Int,
        version: Int,
    ): Flow<List<VerseTranslation>> = context.getTranslationDatabase(edition.slug)
        .ayahDao
        .getByV1Page(page)
        .onEach { it.ifEmpty { downloadQuranTranslation(edition) } }

    override suspend fun getQuranTranslation(
        translation: TranslationEdition,
        key: VerseKey
    ): VerseTranslation? = context.getTranslationDatabase(translation.slug)
        .ayahDao.getAyah(key)
}