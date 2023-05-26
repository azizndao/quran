package arg.quran.models

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

sealed interface UiState<out T> {
  data class Success<T>(val data: T) : UiState<T>
  data class Error(val exception: Throwable? = null, val retry: () -> Unit = {}) : UiState<Nothing>
  object Loading : UiState<Nothing>
}

fun <T> Flow<T>.asUiState(
  scope: CoroutineScope,
  stared: SharingStarted = SharingStarted.WhileSubscribed(5_000),
  initialValue: UiState<T> = UiState.Loading,
): StateFlow<UiState<T>> {
  return this
    .map<T, UiState<T>> { UiState.Success(it) }
    .onStart { emit(UiState.Loading) }
    .catch { emit(UiState.Error(it)) }
    .stateIn(scope, stared, initialValue)
}
