package org.quran.features.share

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

sealed class ShareAyahUiState {
  object Loading : ShareAyahUiState()

  data class Success(
    val sura: Int,
    val surahName: String,
    val ayah: Int,
    val text: String? = null,
    val page: Int,
    val translation: String? = null,
    val textAlign: TextAlign = TextAlign.Center,
    val translationFontSize: TextUnit? = 0.sp,
    val arabicFontSize: TextUnit? = 0.sp,
    val backgroundColor: Color? = null,
    val foregroundColor: Color? = null,
  ) : ShareAyahUiState()
}
