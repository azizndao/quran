package org.alquran.ui.uistate

import arg.quran.models.SuraWithTranslation
import arg.quran.models.SurahMapping


data class SurahListUiState(
  val loading: Boolean = true,
  val recentSurah: SuraWithTranslation? = null,
  val juzs: List<SurahMapping> = emptyList(),
  val exception: Exception? = null,
)