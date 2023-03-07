package org.quran.translation.repositories

import androidx.work.Operation
import arg.quran.models.quran.VerseKey
import arg.quran.models.quran.VerseTranslation
import kotlinx.coroutines.flow.Flow
import org.quran.datastore.TranslationEdition

interface TranslationRepository {

  fun getAvailableTranslations(): Flow<List<TranslationEdition>>

  suspend fun getTranslation(slug: String): TranslationEdition?

  suspend fun downloadTranslations()

  suspend fun downloadQuranTranslation(translation: TranslationEdition): Operation

  fun getQuranTranslations(
    slug: String,
    page: Int,
  ): Flow<List<VerseTranslation>>

  fun getQuranTranslations(
    editions: List<TranslationEdition>,
    page: Int,
  ): Flow<List<TranslationAndVerses>>

  fun getQuranTranslations(
    edition: TranslationEdition,
    page: Int,
  ): Flow<List<VerseTranslation>>

  suspend fun getQuranTranslation(
    translation: TranslationEdition,
    key: VerseKey,
  ): VerseTranslation?

  suspend fun getAyahTranslation(key: VerseKey): VerseTranslation?

  suspend fun getAyahTranslation(key: VerseKey, slug: String): VerseTranslation?

  fun getSelectedTranslations(): Flow<List<TranslationEdition>>
}