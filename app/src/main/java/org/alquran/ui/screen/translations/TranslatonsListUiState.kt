package org.alquran.ui.screen.translations

import org.alquran.utils.getLanguageFlag
import org.quran.datastore.LanguageDirection
import org.quran.datastore.LocaleTranslation

data class TranslationsListUiState(
  val loading: Boolean = true,
  val exception: String? = null,
  val selectedTranslations: List<LocaleTranslation> = emptyList(),
  val editions: List<LocalEditions> = emptyList()
)

data class LocalEditions(
  val displayLanguage: String,
  val editions: List<TranslationUiState>
)


data class TranslationUiState(
  val id: Int,
  val name: String,
  val authorName: String,
  val slug: String,
  val languageCode: String,
  val direction: LanguageDirection,
  val downloaded: Boolean,
  val selected: Boolean,
  val flagUrl: String = getLanguageFlag(languageCode),
)


fun LocaleTranslation.toUiState(selected: Boolean) = TranslationUiState(
  id = id,
  name = name,
  authorName = authorName,
  slug = slug,
  languageCode = languageCode,
  direction = direction,
  downloaded = downloaded,
  selected = selected
)