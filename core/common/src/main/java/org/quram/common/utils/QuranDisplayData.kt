package org.quram.common.utils

import android.content.Context
import android.text.TextUtils
import androidx.annotation.StringRes
import arg.quran.models.quran.VerseKey
import org.quram.common.R
import org.quram.common.core.QuranInfo
import timber.log.Timber

class QuranDisplayData internal constructor(
  private val quranInfo: QuranInfo,
  private val context: Context,
) {

  private val resources = context.resources

  /**
   * Get localized sura name from resources
   *
   * @param sura       Sura number (1~114)
   * @param wantPrefix Whether or not to show prefix "Sura"
   * @return Compiled sura name without translations
   */
  fun getSuraName(sura: Int, wantPrefix: Boolean): String {
    return getSuraName(sura, wantPrefix, false)
  }

  /**
   * Get localized sura name from resources
   *
   * @param sura            Sura number (1~114)
   * @param wantPrefix      Whether or not to show prefix "Sura"
   * @param wantTranslation Whether or not to show sura name translations
   * @return Compiled sura name based on provided arguments
   */
  fun getSuraName(
    sura: Int, wantPrefix: Boolean, wantTranslation: Boolean,
  ): String {
    if (sura < Constants.SURA_FIRST || sura > Constants.SURA_LAST) return ""

    val builder = StringBuilder()
    val suraNames = resources.getStringArray(R.array.sura_names_complex)
    if (wantPrefix) {
      builder.append(
        resources.getString(
          R.string.surah,
          suraNames[sura - 1]
        )
      )
    } else {
      builder.append(suraNames[sura - 1])
    }
    if (wantTranslation) {
      val translation = resources.getStringArray(R.array.sura_translations)[sura - 1]
      if (!TextUtils.isEmpty(translation)) {
        // Some sura names may not have translation
        builder.append(" (").append(translation).append(")")
      }
    }

    return builder.toString()
  }

  fun getSuraNameFromPage(page: Int, wantTitle: Boolean): String {
    val sura = quranInfo.getSuraNumberFromPage(page)
    return if (sura > 0) getSuraName(sura, wantTitle, false) else ""
  }

  fun getPageSubtitle(page: Int): String {
    val description = resources.getString(R.string.page_description)
    return String.format(
      description,
      QuranUtils.getLocalizedNumber(page),
      QuranUtils.getLocalizedNumber(quranInfo.getJuzForDisplayFromPage(page))
    )
  }

  fun getJuzDisplayStringForPage(page: Int): String {
    val description = resources.getString(R.string.juz2_description)
    return String.format(
      description,
      QuranUtils.getLocalizedNumber(quranInfo.getJuzForDisplayFromPage(page))
    )
  }

  fun getSuraAyahString(sura: Int, ayah: Int): String {
    return getSuraAyahString(sura, ayah, R.string.sura_ayah_notification_str)
  }

  fun getSuraAyahString(
    sura: Int,
    ayah: Int,
    @StringRes resource: Int,
  ): String {
    val suraName = getSuraName(sura, wantPrefix = false, wantTranslation = false)
    return resources.getString(resource, suraName, sura, ayah)
  }

  fun getSuraAyahStringForSharing(sura: Int, ayah: Int): String {
    val suraName = getSuraName(sura, wantPrefix = false, wantTranslation = false)
    return resources.getString(R.string.sura_ayah_sharing_str, suraName, ayah)
  }

  fun getNotificationTitle(minVerse: VerseKey, maxVerse: VerseKey, isGapless: Boolean): String {
    val minSura = minVerse.sura
    var maxSura = maxVerse.sura

    val notificationTitle =
      getSuraName(minSura, wantPrefix = true, wantTranslation = false)
    if (isGapless) {
      // for gapless, don't show the ayah numbers since we're
      // downloading the entire sura(s).
      return if (minSura == maxSura) {
        notificationTitle
      } else {
        "$notificationTitle - " + getSuraName(
          maxSura, wantPrefix = true, wantTranslation = false
        )
      }
    }

    var maxAyah = maxVerse.aya
    if (maxAyah == 0) {
      maxSura--
      maxAyah = quranInfo.getNumberOfAyahs(maxSura)
    }

    return notificationTitle.plus(
      if (minSura == maxSura) {
        if (minVerse.aya == maxAyah) {
          " ($maxAyah)"
        } else {
          " (" + minVerse.aya + "-" + maxAyah + ")"
        }
      } else {
        " (" + minVerse.aya + ") - " +
          getSuraName(maxSura, wantPrefix = true, wantTranslation = false) +
          " (" + maxAyah + ")"
      }
    )
  }

  fun getSuraListMetaString(sura: Int): String {
    val info =
      resources.getString(if (quranInfo.isMakki(sura)) R.string.makki else R.string.madani) + " - "

    val ayahs = quranInfo.getNumberOfAyahs(sura)
    return info + resources.getQuantityString(
      R.plurals.verses, ayahs,
      QuranUtils.getLocalizedNumber(ayahs)
    )
  }

  fun safelyGetSuraOnPage(page: Int): Int {
    return if (page < Constants.PAGES_FIRST || page > quranInfo.numberOfPages) {
      Timber.e(IllegalArgumentException("safelyGetSuraOnPage with page: $page"))
      quranInfo.getSuraOnPage(1)
    } else {
      quranInfo.getSuraOnPage(page)
    }
  }

  fun getSuraNameFromPage(page: Int): String {
    val suraNumber = quranInfo.getSuraNumberFromPage(page)
    return getSuraName(suraNumber, wantPrefix = false, wantTranslation = false)
  }

  fun getAyahString(sura: Int, ayah: Int): String {
    return getSuraName(sura, true) + " - " + context.getString(R.string.quran_ayah, ayah)
  }

  fun getAyahMetadata(sura: Int, ayah: Int, page: Int): String {
    val juz = quranInfo.getJuzForDisplayFromPage(page)
    return context.getString(
      R.string.quran_ayah_details,
      getSuraName(sura, true),
      QuranUtils.getLocalizedNumber(ayah),
      QuranUtils.getLocalizedNumber(quranInfo.getJuzFromSuraAyah(sura, ayah, juz))
    )
  }

  // do not remove the nullable return type
  fun getSuraNameString(page: Int): String {
    return context.getString(R.string.quran_sura_title, getSuraNameFromPage(page))
  }

  fun getAyahKeysOnPage(page: Int): MutableSet<VerseKey> {
    val ayahKeys: MutableSet<VerseKey> = LinkedHashSet()
    val bounds = quranInfo.getPageBounds(page)
    val start = VerseKey(bounds[0], bounds[1])
    val end = VerseKey(bounds[2], bounds[3])

    val iterator = SuraAyahIterator(quranInfo, start, end)
    while (iterator.next()) {
      ayahKeys.add(VerseKey(iterator.sura, iterator.ayah))
    }
    return ayahKeys
  }
}
