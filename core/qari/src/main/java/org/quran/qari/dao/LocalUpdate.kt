package org.quran.qari.dao

import org.quran.qari.model.QariItem

data class LocalUpdate(
  val qari: QariItem,
  val files: List<String> = emptyList(),
  val needsDatabaseUpgrade: Boolean = false
)
