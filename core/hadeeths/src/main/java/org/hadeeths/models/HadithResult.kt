package org.hadeeths.models

data class HadithResult(
  val id: Int,
  val snippet: String,
  val language: String,
  val categoryId: Int,
  val categoryName: String,
)
