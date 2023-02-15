package org.quran.translation.repositories

import androidx.work.Operation
import kotlinx.coroutines.flow.Flow
import org.quram.common.model.VerseKey
import org.quran.datastore.TranslationEdition
import org.quran.translation.model.VerseTranslation

interface TranslationRepository {

    fun getAvailableTranslations(): Flow<List<TranslationEdition>>

    suspend fun getTranslation(slug: String): TranslationEdition?

    suspend fun downloadTranslations()

    suspend fun downloadQuranTranslation(translation: TranslationEdition): Operation

    fun getQuranTranslations(
        slug: String,
        page: Int,
        version: Int,
    ): Flow<List<VerseTranslation>>

    fun getQuranTranslations(
        editions: List<TranslationEdition>,
        page: Int,
        version: Int,
    ): Flow<Array<List<VerseTranslation>>>

    fun getQuranTranslations(
        edition: TranslationEdition,
        page: Int,
        version: Int,
    ): Flow<List<VerseTranslation>>

    suspend fun getQuranTranslation(
        translation: TranslationEdition,
        key: VerseKey,
    ): VerseTranslation?

    suspend fun getAyahTranslation(key: VerseKey): VerseTranslation?

    suspend fun getAyahTranslation(key: VerseKey, slug: String): VerseTranslation?
}