package org.alquran.ui.screen.home.juzs

import arg.quran.models.HizbQuarter
import arg.quran.models.QuarterMapping
import org.quran.datastore.QuranPosition

val HizbQuarter.position
  get() = QuranPosition(
    surahNumber,
    ayahNumberInSurah,
    page
  )

data class JuzListUiState(
  val loading: Boolean = true,
  val data: List<QuarterMapping> = emptyList(),
  val exception: Exception? = null
)
