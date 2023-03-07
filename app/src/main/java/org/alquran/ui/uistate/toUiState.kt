package org.alquran.ui.uistate

import arg.quran.models.quran.CharType
import arg.quran.models.quran.VerseKey
import arg.quran.models.quran.VerseWord
import arg.quran.models.quran.WordTranslation

data class WordUiState(
  val id: Int,
  val position: Int,
  val key: VerseKey,
  val audioURL: String? = null,
  val charType: CharType,
  val line: Int,
  val text: String,
  val translation: WordTranslation,
  val transliteration: WordTranslation,
  val playing: Boolean,
  val selected: Boolean
)

fun VerseWord.toUiState(playing: Boolean, selected: Boolean) = WordUiState(
  id = id,
  position = position,
  key = key,
  audioURL = audioURL,
  charType = charType,
  line = line,
  text = codeV1!!,
  translation = translation,
  transliteration = transliteration,
  playing = playing,
  selected = selected
)