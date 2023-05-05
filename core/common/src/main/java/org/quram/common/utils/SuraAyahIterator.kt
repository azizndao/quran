package org.quram.common.utils

import arg.quran.models.quran.VerseKey
import org.quram.common.core.QuranInfo


class SuraAyahIterator(
  private val quranInfo: QuranInfo,
  start: VerseKey,
  end: VerseKey
) {
  private val start: VerseKey
  private val end: VerseKey
  private var started = false

  var sura = 0
    private set
  var ayah = 0
    private set

  val curAyah: VerseKey
    get() = VerseKey(sura, ayah)

  init {
    // Sanity check
    if ((start.compareTo(end)) <= 0) {
      this.start = start
      this.end = end
    } else {
      this.start = end
      this.end = start
    }
    reset()
  }

  private fun reset() {
    sura = start.sura
    ayah = start.aya
    started = false
  }

  private operator fun hasNext(): Boolean {
    return !started || sura < end.sura || ayah < end.aya
  }

  operator fun next(): Boolean {
    if (!started) {
      started = true
      return true
    } else if (!hasNext()) {
      return false
    }
    if (ayah < quranInfo.getNumberOfAyahs(sura)) {
      ayah++
    } else {
      ayah = 1
      sura++
    }
    return true
  }
}
