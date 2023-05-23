package org.quran.features.home.juzs

import arg.quran.models.HizbQuarter
import arg.quran.models.QuarterMapping
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import org.quran.datastore.QuranPosition

val HizbQuarter.position
  get() = QuranPosition(
    surahNumber,
    ayahNumberInSurah,
    page
  )

data class JuzListUiState(
  val loading: Boolean = true,
  val data: PersistentList<QuarterMapping> = persistentListOf(),
  val exception: Exception? = null
)
