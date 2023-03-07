package org.quram.common.utils

import android.content.Context
import org.quram.common.R
import org.quram.common.utils.QuranUtils.getLocalizedNumber

class Rub3DisplayUseCase internal constructor(
  private val context: Context,
  private val quranInfo: QuranInfo,
) {
  // same logic used in displayMarkerPopup method
  operator fun invoke(page: Int): String {
    val rub3 = quranInfo.getRub3FromPage(page)
    if (rub3 == -1) return ""

    val hizb = rub3 / 4 + 1
    val sb = StringBuilder().append(context.getString(R.string.comma_with_spaces))
    when (rub3 % 4) {
      1 -> sb.append(context.getString(R.string.quran_rob3)).append(' ')
      2 -> sb.append(context.getString(R.string.quran_nos)).append(' ')
      3 -> sb.append(context.getString(R.string.quran_talt_arb3)).append(' ')
    }
    sb.append(context.getString(R.string.quran_hizb)).append(' ')
      .append(getLocalizedNumber(hizb))
    return sb.toString()
  }
}