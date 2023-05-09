package org.quran.translation.repositories

import androidx.work.Operation
import arg.quran.models.quran.Verse
import arg.quran.models.quran.VerseKey
import kotlinx.coroutines.flow.Flow
import org.quran.datastore.LocaleTranslation
import org.quran.translation.local.models.TranslatedEdition

interface TranslationRepository {

  fun getAvailableTranslations(): Flow<List<LocaleTranslation>>

  suspend fun getTranslation(slug: String): LocaleTranslation?

  suspend fun downloadTranslations()

  suspend fun downloadQuranTranslation(slug: String): Operation

  suspend fun getQuranTranslation(
    translation: LocaleTranslation,
    key: VerseKey,
  ): Verse?

  suspend fun getAyahTranslation(key: VerseKey): Verse?

  suspend fun getAyahTranslation(key: VerseKey, slug: String): Verse?

  fun getSelectedTranslations(): Flow<List<LocaleTranslation>>

  suspend fun search(query: String): List<TranslatedEdition>

  suspend fun search(locale: LocaleTranslation, query: String): List<Verse>

  fun getAllVerses(locale: LocaleTranslation): Flow<List<Verse>>
  fun getSelectedTranslationSlugs(): Flow<List<String>>
}