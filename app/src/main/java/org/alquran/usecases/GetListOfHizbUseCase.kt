package org.alquran.usecases

import arg.quran.models.HizbQuarter
import arg.quran.models.QuarterMapping
import arg.quran.models.quran.VerseKey
import org.quram.common.repositories.SurahRepository
import org.quram.common.utils.QuranInfo

class GetListOfHizbUseCase(
  private val quranInfo: QuranInfo,
  private val surahRepository: SurahRepository,
) {

  operator fun invoke(): List<QuarterMapping> = buildList {
    val quarters: Array<VerseKey> = quranInfo.quarters
    val surahs = surahRepository.getAllSurahs()
    var index = 0
    for (j in 1..30) {
      val rub3s = List(8) {
        val (sura, ayah) = quarters[index++]
        HizbQuarter(
          surahName = surahs[sura - 1].nameSimple,
          surahNumber = sura,
          ayahNumberInSurah = ayah,
          juz = j,
          page = quranInfo.getPageFromSuraAyah(sura, ayah),
          hizbQuarter = index + 1
        )
      }

      add(
        QuarterMapping(
          number = j,
          page = rub3s.first().page,
          quarters = rub3s,
        )
      )
    }
  }
}