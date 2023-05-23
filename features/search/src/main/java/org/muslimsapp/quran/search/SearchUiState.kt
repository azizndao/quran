package org.muslimsapp.quran.search

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import org.quran.datastore.LocaleTranslation

data class SearchUiState(
  val loading: Boolean = false,
  val query: String = "",
  val onQueryChange: (String) -> Unit,
  val active: Boolean = false,
  val onActiveChange: (Boolean) -> Unit,
  val selectedEdition: String? = null,
  val translations: PersistentList<LocaleTranslation> = persistentListOf(),
  val results: PersistentList<SearchResult> = persistentListOf(),
  val getPage: (SearchResult) -> Int,
  val setSelectedEdition: (slug: String?) -> Unit,
)

data class SearchResult(
  val sura: Int,
  val ayah: Int,
  val text: String,
  val type: Type,
  val name: String,
) {
  enum class Type { QURAN, TRANSLATION }
}