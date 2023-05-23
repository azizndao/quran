package org.quran.features.home.surahs

import arg.quran.models.SuraWithTranslation
import arg.quran.models.SurahMapping
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf


data class SurahListUiState(
  val loading: Boolean = true,
  val recentSurah: SuraWithTranslation? = null,
  val juzs: PersistentList<SurahMapping> = persistentListOf(),
  val exception: Exception? = null,
)