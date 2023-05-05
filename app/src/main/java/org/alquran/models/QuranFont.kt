package org.alquran.models

import androidx.annotation.StringRes
import androidx.compose.ui.text.font.FontFamily
import org.alquran.R
import org.quran.ui.theme.QuranFontFamilies


enum class QuranFont(val fontFamily: FontFamily, @StringRes val nameId: Int) {
  HafsSmartV8(QuranFontFamilies.Hafs, R.string.uthmani_hafs_font),
}
