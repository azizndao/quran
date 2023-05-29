package org.quran.features.pager.uiState

sealed class PageItem {
  abstract val page: Int
  abstract val header: Header

  data class Header(val leading: String, val trailing: String)
}

