package org.quran.network.translation

import arg.quran.models.Language
import arg.quran.models.quran.Verse
import org.quran.network.translation.models.ApiTranslation

interface TranslationApiService {

  suspend fun getAvailableLanguages(language: String = "en"): List<Language>

  suspend fun getAvailableTranslations(language: String = "en"): List<ApiTranslation>

  suspend fun getVerses(translationId: Int): List<Verse>

}