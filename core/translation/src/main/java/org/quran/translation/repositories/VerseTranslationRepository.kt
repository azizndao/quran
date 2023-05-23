package org.quran.translation.repositories

import arg.quran.models.quran.Verse
import kotlinx.coroutines.flow.Flow
import org.quran.datastore.LocaleTranslation

interface VerseTranslationRepository {

  suspend fun enqueueDownload(slug: String)

  fun getQuranTranslations(
    translation: LocaleTranslation,
    limit: Int,
    offset: Int
  ): Flow<List<Verse>>

  suspend fun getQuranTranslation(
    translation: LocaleTranslation,
    sura: Int,
    ayah: Int
  ): Verse?

  fun getAll(slug: String): Flow<List<String>>

  suspend fun getAyahTranslation(sura: Int, ayah: Int): Verse?

  suspend fun getAyahTranslation(sura: Int, ayah: Int, slug: String): Verse?

  suspend fun search(translationId: String, query: String): List<Verse>

  fun observeAllVerse(slug: String): Flow<List<Verse>>

  suspend fun downloadTranslations(translation: LocaleTranslation)

  suspend fun downloadTranslations(translation: String)
}