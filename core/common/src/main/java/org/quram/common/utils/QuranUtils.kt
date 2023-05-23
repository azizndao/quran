package org.quram.common.utils

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

object QuranUtils {
  private var isArabicFormatter = false
  private var numberFormat: NumberFormat? = null
  private var lastLocale: Locale? = null

  /**
   * Returns a boolean indicating if this string contains Arabic
   * Note that this returns true for non-Arabic languages that share
   * Arabic characters (ex Urdu).
   *
   * @param s the string to check
   * @return a boolean
   */
  fun doesStringContainArabic(s: String?): Boolean {
    if (s == null) {
      return false
    }
    val length = s.length
    for (i in 0 until length) {
      val current = s[i].code
      // Skip space
      if (current == 32) {
        continue
      }
      // non-reshaped arabic
      if (current in 1570..1610) {
        return true
      } else if (current in 65133..65276) {
        return true
      } else if (current != 42 && current != 40 && current != 41 && current != 91 && current != 93) {
        return false
      }
    }
    return false
  }

  val isRtl: Boolean
    get() = (TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault())
      == ViewCompat.LAYOUT_DIRECTION_RTL)

  fun getLocalizedNumber(number: Int): String {
    val locale = Locale.getDefault()
    val change = numberFormat == null || locale != lastLocale
    if (change) {
      numberFormat = DecimalFormat.getIntegerInstance(locale)
      lastLocale = locale
    }
    return numberFormat!!.format(number.toLong())
  }
}