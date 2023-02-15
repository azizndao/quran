package org.alquran.usecases

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import org.muslimapp.domain.quran.uistate.SurahListUiState
import org.quram.common.repositories.SurahRepository


class ListSurahUseCase(
    private val surahRepository: SurahRepository,
) {

    operator fun invoke(scope: CoroutineScope): StateFlow<SurahListUiState> {
        return flow {
            val surahs = surahRepository.getJuzsSurahs()
            if (surahs.isEmpty()) return@flow emit(SurahListUiState())

            emit(SurahListUiState(loading = false, recentSurah = null, juzs = surahs))
        }.flowOn(Dispatchers.IO).stateIn(scope, SharingStarted.Eagerly, SurahListUiState())
    }
}