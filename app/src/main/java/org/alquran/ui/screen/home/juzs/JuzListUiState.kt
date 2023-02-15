package org.alquran.ui.screen.home.juzs

import org.quran.datastore.QuranPosition
import org.quram.common.model.HizbQuarter
import org.quram.common.model.QuarterMapping

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
