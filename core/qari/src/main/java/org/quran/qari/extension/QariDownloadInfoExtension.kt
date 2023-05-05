package org.quran.qari.extension

import arg.quran.models.quran.VerseKey
import org.quran.qari.model.QariDownloadInfo

/**
 * Determine whether the range of verses is downloaded
 * This will check, for gapless files, if every sura is fully downloaded or not.
 * For gapped files, it will do the aforementioned check, and will alternatively
 * check that all ayat in the range are downloaded.
 *
 * Note that "partially downloaded gapless files" are not really a portion today.
 * Those are simply determined by a .part file existing, and we don't try to figure
 * out which portion of the file is there and which isn't since playback of downloaded
 * gapless files is all or none.
 */
fun QariDownloadInfo.isRangeDownloaded(start: VerseKey, end: VerseKey): Boolean {
  val suraRange = IntRange(start.sura, end.sura)
  return when (this) {
    is QariDownloadInfo.GaplessQariDownloadInfo -> suraRange.all { it in fullyDownloadedSuras }
    is QariDownloadInfo.GappedQariDownloadInfo -> {
      suraRange.all { sura ->
        if (sura in fullyDownloadedSuras) {
          true
        } else {
          val start = if (start.sura == sura) start.aya else 1
          val partiallyDownloadedSuraInfo = partiallyDownloadedSuras.firstOrNull { it.sura == sura }
          if (partiallyDownloadedSuraInfo != null) {
            val end = if (end.sura == sura) end.aya else partiallyDownloadedSuraInfo.expectedAyahCount
            partiallyDownloadedSuraInfo.didDownloadAyat(sura, start, end)
          } else {
            false
          }
        }
      }
    }
  }
}
