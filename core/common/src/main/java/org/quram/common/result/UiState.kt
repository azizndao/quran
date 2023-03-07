package org.quram.common.result

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import timber.log.Timber

sealed interface UiState<out T> {
  data class Success<T>(val data: T) : UiState<T>
  data class Error(val exception: Throwable? = null) : UiState<Nothing>
  object Loading : UiState<Nothing>
}

fun <T> Flow<T>.asUiState(
  scope: CoroutineScope,
  stared: SharingStarted = SharingStarted.Eagerly,
  initialValue: UiState<T> = UiState.Loading,
): StateFlow<UiState<T>> {
  return this
    .map<T, UiState<T>> { UiState.Success(it) }
    .onStart { emit(UiState.Loading) }
    .catch { Timber.e(it); emit(UiState.Error(it)) }
    .stateIn(scope, stared, initialValue)
}
