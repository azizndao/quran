package org.alquran.ui.screen.search

import arg.quran.models.quran.Verse

data class SearchUiState(
  val active: Boolean,
  val onActiveChange: (Boolean) -> Unit,
  val query: String,
  val onQueryChange: (String) -> Unit,
  val results: List<SearchResult>
)

data class SearchResult(
  override val sura: Int,
  override val ayah: Int,
  override val text: String
) : Verse()