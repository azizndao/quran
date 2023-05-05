package org.quran.translation.repositories

import androidx.work.Operation
import arg.quran.models.quran.Verse
import arg.quran.models.quran.VerseKey
import kotlinx.coroutines.flow.Flow
import org.quran.datastore.LocaleTranslation
import org.quran.translation.models.TranslatedEdition

interface TranslationRepository {

  fun getAvailableTranslations(): Flow<List<LocaleTranslation>>

  suspend fun getTranslation(slug: String): LocaleTranslation?

  suspend fun downloadTranslations()

  suspend fun downloadQuranTranslation(translation: LocaleTranslation): Operation

  fun getVerses(
    slug: String,
    page: Int,
  ): Flow<TranslatedEdition?>

  fun getSelectedEditions(page: Int): Flow<List<TranslatedEdition>>

  fun getVerses(
    edition: LocaleTranslation,
    page: Int,
  ): Flow<TranslatedEdition?>

  suspend fun getQuranTranslation(
    translation: LocaleTranslation,
    key: VerseKey,
  ): Verse?

  suspend fun getAyahTranslation(key: VerseKey): Verse?

  suspend fun getAyahTranslation(key: VerseKey, slug: String): Verse?

  fun getSelectedTranslations(): Flow<List<LocaleTranslation>>
}