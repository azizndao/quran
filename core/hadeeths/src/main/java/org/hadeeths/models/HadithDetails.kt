package org.hadeeths.models

import androidx.room.Embedded

data class HadithDetails(
  val id: Int,
  val title: String,
  val text: String,
  val textArabic: String,
  val attribution: String,
  val grade: String,
  val explanation: String,
  val categories: List<String>,
  val language: String,
  val otherLanguages: List<String>,
  val reference: String? = null,
  val wordsMeanings: List<WordsMeaning> = emptyList(),
  val hints: List<String> = emptyList(),
  @Embedded val bookmark: HadithBookmark? = null,
)
