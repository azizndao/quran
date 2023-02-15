package org.alquran.hafs.repository

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import org.alquran.hafs.databases.QuranDatabase
import org.alquran.hafs.model.Verse
import org.alquran.hafs.model.VerseTranslation
import org.alquran.hafs.model.VerseWithTranslation
import org.alquran.hafs.model.toDbModel
import org.quram.common.utils.UriProvider
import org.quran.datastore.repositories.QuranPreferencesRepository
import org.quran.datastore.serializers.DEFAULT_TRANSLATION_IDS
import org.quran.network.model.ApiVerse
import org.quran.network.model.ApiVerseTranslation
import org.quran.network.services.VerseApiService
import timber.log.Timber


class VerseRepository internal constructor(
    private val apiService: VerseApiService,
    private val quranPreferences: QuranPreferencesRepository,
    private val context: Context,
) {

    suspend fun hasMissingVerses(version: Int) =
        QuranDatabase.getInstance(context, version).dao.hasMissingVerses()

    suspend fun insertVerses(items: List<Verse>, version: Int) =
        QuranDatabase.getInstance(context, version).dao.insertVerses(items)

    fun getVerses(page: Int, version: Int): Flow<List<Verse>> =
        QuranDatabase.getInstance(context, version).dao.getVersesFlow(page).onEach { verses ->
            verses.ifEmpty { downloadVerseByPage(page, version, DEFAULT_TRANSLATION_IDS) }
        }

    fun getVersesWithTranslations(
        page: Int,
        version: Int
    ): Flow<List<VerseWithTranslation>> {
        return QuranDatabase.getInstance(context, version).dao.getVersesWithTranslations(page)
            .onEach { verses ->
                verses.ifEmpty {
                    val translations = quranPreferences.getSelectedTranslationId().first()
                    downloadVerseByPage(
                        page,
                        version,
                        translations.ifEmpty { DEFAULT_TRANSLATION_IDS })
                }
            }
    }

    private suspend fun downloadVerseByPage(page: Int, version: Int, translations: List<Int>) {
        Timber.tag("VerseRepository").d("Downloading v1 page content $page")

//        apiService.downloadFont(page, version, UriProvider.getFontFile(context, page, version))

        val apiVerses = apiService.downloadVerseByPage(page, version, translations)
        val verses = apiVerses.map(ApiVerse::toDbModel)

        val verseTranslations = apiVerses.fold(mutableListOf<VerseTranslation>()) { acc, apiVerse ->
            acc.addAll(apiVerse.translations.map(ApiVerseTranslation::toDbModel))
            acc
        }

        val dao = QuranDatabase.getInstance(context, version).dao
        dao.insertVerses(verses)
        dao.insertTranslation(verseTranslations)
    }

    suspend fun insertTranslations(
        context: Context,
        version: Int,
        translations: MutableList<VerseTranslation>
    ) {
        QuranDatabase.getInstance(context, version).dao.insertTranslation(translations)
    }
}