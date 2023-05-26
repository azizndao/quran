package org.quran.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import kotlinx.coroutines.CoroutineExceptionHandler
import org.quran.ui.R
import timber.log.Timber

object QuranFontFamilies {
  val SuraNames = FontFamily(Font(R.font.sura_names))

  val Hafs by lazy { FontFamily(Font(R.font.hafs_smart_08)) }

  val WorkSans = FontFamily(Font(R.font.worksans_bold))

  val handler = CoroutineExceptionHandler { _, throwable ->
    Timber.tag("MuslimsFontFamilies").e(throwable)
  }
}
