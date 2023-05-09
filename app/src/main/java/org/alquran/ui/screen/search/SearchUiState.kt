package org.alquran.ui.screen.search

import arg.quran.models.quran.Verse

data class SearchUiState(
  val active: Boolean,
  val onActiveChange: (Boolean) -> Unit,
  val query: String,
  val onQueryChange: (String) -> Unit,
  val results: List<SearchResult>,
  val loading: Boolean,
)

data class SearchResult(
  override val sura: Int,
  override val ayah: Int,
  override val text: String,
  val authorName: String,
  val subtext: String,
  val type: SearchResultType
) : Verse()

enum class SearchResultType {
  QURAN, ARABIC, LATIN
}