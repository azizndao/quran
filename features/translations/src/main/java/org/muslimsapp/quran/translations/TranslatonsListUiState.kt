package org.muslimsapp.quran.translations

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import org.alquran.utils.getLanguageFlag
import org.quran.datastore.LocaleTranslation

data class TranslationsListUiState(
  val loading: Boolean = true,
  val selectedTranslation: String? = null,
  val exception: String? = null,
  val downloadedTranslations: PersistentList<TranslationUiState> = persistentListOf(),
  val locales: PersistentList<LocaleSection> = persistentListOf(),
  val clearSelection: () -> Unit = {},
  val moveUp: () -> Unit = {},
  val moveDown: () -> Unit = {},
  val delete: () -> Unit = {},
)


data class LocaleSection(
  val displayLanguage: String,
  val translations: List<TranslationUiState>
)

data class TranslationUiState(
  val id: Int = 0,
  val name: String,
  val authorName: String,
  val languageCode: String,
  val slug: String,
  val downloaded: Boolean,
  val flagUrl: String,
  val enabled: Boolean,
  val selected: Boolean,
  val onClick: () -> Unit,
  val onLongClick: () -> Unit,
)


fun LocaleTranslation.toUiState(
  enabled: Boolean,
  selected: Boolean,
  onClick: () -> Unit,
  onLonClick: () -> Unit,
): TranslationUiState {
  return TranslationUiState(
    id = id,
    name = name,
    authorName = authorName,
    languageCode = languageCode,
    slug = slug,
    downloaded = downloaded,
    flagUrl = getLanguageFlag(languageCode),
    enabled = enabled,
    onClick = onClick,
    onLongClick = onLonClick,
    selected = selected,
  )
}