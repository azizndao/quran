package org.quran.qari.extension

import org.quran.qari.model.PartiallyDownloadedSura

fun PartiallyDownloadedSura.didDownloadAyat(currentSura: Int, start: Int, end: Int): Boolean {
  val ayat = IntRange(start, end)
  return sura == currentSura && ayat.all { it in downloadedAyat }
}
