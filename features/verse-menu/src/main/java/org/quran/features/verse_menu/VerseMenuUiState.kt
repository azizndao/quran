package org.quran.features.verse_menu

import arg.quran.models.quran.VerseKey

data class VerseMenuArgs(
  val key: VerseKey,
  val word: Int?,
)

data class VerseMenuUiState(
  val label: String,
  val key: VerseKey,
  val isBookmarked: Boolean,
  val word: Int?,
  val onEvent: (VerseMenuEvent) -> Unit
)

sealed class VerseMenuEvent {

  object Play : VerseMenuEvent()

  object PlayWord : VerseMenuEvent()

  object Repeat : VerseMenuEvent()

  object Bookmark : VerseMenuEvent()

  object Copy : VerseMenuEvent()
}
