package org.quran.features.pager.uiState

import arg.quran.models.quran.CharType
import arg.quran.models.quran.QuranWord
import arg.quran.models.quran.VerseKey

data class WordUiState(
  val id: Int,
  val position: Int,
  val key: VerseKey,
  val audioURL: String? = null,
  val charType: CharType,
  val line: Int,
  val text: String,
  val playing: Boolean,
  val selected: Boolean,
  val bookmarked: Boolean
)

fun QuranWord.toUiState(playing: Boolean, selected: Boolean, bookmarked: Boolean) = WordUiState(
  id = id,
  position = position,
  key = key,
  charType = type,
  line = line,
  text = text,
  playing = playing,
  selected = selected,
  bookmarked = bookmarked,
)