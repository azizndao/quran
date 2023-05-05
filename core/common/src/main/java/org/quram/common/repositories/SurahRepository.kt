package org.quram.common.repositories

import android.content.Context
import arg.quran.models.SuraWithTranslation
import arg.quran.models.SurahMapping
import org.quram.common.R
import org.quram.common.datasources.SurahsDataSources
import org.quram.common.source.QuranDataSource

class SurahRepository(
  private val context: Context,
  private val quranDataSource: QuranDataSource,
) {

  private var surahs: List<SuraWithTranslation>

  init {
    val translations = context.resources.getStringArray(R.array.sura_translations)
    surahs = SurahsDataSources.mapIndexed { index, surah ->
      SuraWithTranslation(
        number = surah.number,
        nameArabic = surah.nameArabic,
        nameComplex = surah.nameComplex,
        nameSimple = surah.nameSimple,
        isMakki = surah.isMakki,
        revelationOrder = surah.revelationOrder,
        translation = translations[index],
        ayahCount = surah.ayahCount,
        pages = surah.pages
      )
    }
  }

  fun getJuzsSurahs(): List<SurahMapping> {
    val pageForJuzArray = quranDataSource.pageForJuzArray
    val juzDisplayPageArrayOverride = quranDataSource.juzDisplayPageArrayOverride
    return quranDataSource.juzsArray.map {
      SurahMapping(juz = it.id,
        page = juzDisplayPageArrayOverride[it.id - 1] ?: pageForJuzArray[it.id - 1],
        surahs = buildList {
          for (m in it.ayahMapping) {
            if (m.value.first == 1) {
              add(surahs[m.key - 1])
            }
          }
        })
    }
  }

  fun getSurah(surah: Int) = SurahsDataSources[surah - 1]

  fun getSurahName(surah: Int) = context.resources.getStringArray(R.array.sura_names)[surah]

  fun getAllSurahs() = SurahsDataSources

  fun getSurahs(ids: List<Int>) = surahs.filter { it.number in ids }

  fun getAllSurahWithTranslation() = surahs

  fun getSurahWithTranslation(surah: Int) = surahs[surah - 1]

  fun getSurahsInPage(page: Int) = SurahsDataSources.filter { page in it.pages }

}