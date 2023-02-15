package org.muslimapp.domain.quran.uistate

import org.quram.common.model.SurahMapping
import org.quram.common.model.SuraWithTranslation


data class SurahListUiState(
    val loading: Boolean = true,
    val recentSurah: SuraWithTranslation? = null,
    val juzs: List<SurahMapping> = emptyList(),
    val exception: Exception? = null,
)