package arg.quran.models

data class HizbQuarter(
  val surahName: String,
  val surahNumber: Int,
  val ayahNumberInSurah: Int,
  val juz: Int,
  val page: Int,
  val hizbQuarter: Int
)