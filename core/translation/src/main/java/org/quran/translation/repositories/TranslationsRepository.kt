package org.quran.translation.repositories

import kotlinx.coroutines.flow.Flow
import org.quran.datastore.LocaleTranslation

interface TranslationsRepository {

  fun observeAvailableTranslations(): Flow<List<LocaleTranslation>>

  fun observeSelectedEditions(): Flow<List<LocaleTranslation>>

  fun observeSelectedTranslationSlugs(): Flow<List<String>>

  suspend fun getSelectedTranslationSlugs(): List<String>

  suspend fun getTranslation(slug: String): LocaleTranslation?

  suspend fun downloadTranslations()

  suspend fun deleteTranslation(slug: String): Boolean

  suspend fun enableTranslation(slug: String)

  suspend fun downloadTranslation(slug: String)

  suspend fun disableTranslation(slug: String)

 suspend fun moveTranslationDown(slug: String)

 suspend fun moveTranslationUp(slug: String)
}