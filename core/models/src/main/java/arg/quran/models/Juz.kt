package arg.quran.models

import arg.quran.models.quran.VerseKey

data class Juz(
  val id: Int,
  val ayahCount: Int,
  val ayahMapping: Map<Int, IntRange>,
)


val Juz.firstAyah: VerseKey
  get() {
    val surah = ayahMapping.keys.first()
    val ayahRange = ayahMapping[surah]!!
    return VerseKey(surah, ayahRange.first)
  }


val Juz.lastAyah: VerseKey
  get() {
    val surah = ayahMapping.keys.last()
    val ayahRange = ayahMapping[surah]!!
    return VerseKey(surah, ayahRange.last)
  }