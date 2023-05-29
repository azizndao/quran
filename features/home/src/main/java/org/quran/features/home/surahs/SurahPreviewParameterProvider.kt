package org.quran.features.home.surahs

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import arg.quran.models.SuraWithTranslation
import arg.quran.models.SurahMapping


class SurahListPreviewParameterProvider : PreviewParameterProvider<List<SurahMapping>> {
  override val values = sequenceOf(
    listOf(
      SurahMapping(
        juz = 1,
        page = 1,
        surahs = listOf(
          SuraWithTranslation(
            number = 1,
            revelationOrder = 5,
            isMakki = true,
            nameSimple = "Al-Fatihah",
            nameComplex = "Al-Fātiĥah",
            nameArabic = "الفاتحة",
            ayahCount = 7,
            pages = 1..1,
            translation = "The Opener",
          ),
          SuraWithTranslation(
            number = 2,
            revelationOrder = 87,
            isMakki = false,
            nameSimple = "Al-Baqarah",
            nameComplex = "Al-Baqarah",
            nameArabic = "البقرة",
            ayahCount = 286,
            pages = 1..49,
            translation = "The Cow",
          ),
        )
      ),
      SurahMapping(
        juz = 2,
        page = 22,
        surahs = emptyList()
      ),
      SurahMapping(
        juz = 3,
        page = 42,
        surahs = listOf(
          SuraWithTranslation(
            number = 3,
            revelationOrder = 89,
            isMakki = false,
            nameSimple = "Ali 'Imran",
            nameComplex = "Āli `Imrān",
            nameArabic = "آل عمران",
            ayahCount = 200,
            pages = 50..76,
            translation = "Family of Imran",
          ),
        )
      ),
      SurahMapping(
        juz = 4,
        page = 62,
        surahs = listOf(
          SuraWithTranslation(
            number = 4,
            revelationOrder = 92,
            isMakki = false,
            nameSimple = "An-Nisa",
            nameComplex = "An-Nisā",
            nameArabic = "النساء",
            ayahCount = 176,
            pages = 77..106,
            translation = "The Women",
          ),
        )
      ),
      SurahMapping(
        juz = 5,
        page = 82,
        surahs = emptyList(),
      ),
      SurahMapping(
        juz = 6,
        page = 102,
        surahs = listOf(
          SuraWithTranslation(
            number = 5,
            revelationOrder = 112,
            isMakki = false,
            nameSimple = "Al-Ma'idah",
            nameComplex = "Al-Mā'idah",
            nameArabic = "المائدة",
            ayahCount = 120,
            pages = 106..127,
            translation = "The Table Spread",
          ),
        ),
      ),
      SurahMapping(
        juz = 7,
        page = 121,
        surahs = listOf(
          SuraWithTranslation(
            number = 6,
            revelationOrder = 55,
            isMakki = true,
            nameSimple = "Al-An'am",
            nameComplex = "Al-'An`ām",
            nameArabic = "الأنعام",
            ayahCount = 165,
            pages = 128..150,
            translation = "The Cattle",
          ),
        ),
      ),
      SurahMapping(
        juz = 8,
        page = 142,
        surahs = listOf(
          SuraWithTranslation(
            number = 7,
            revelationOrder = 39,
            isMakki = true,
            nameSimple = "Al-A'raf",
            nameComplex = "Al-'A`rāf",
            nameArabic = "الأعراف",
            ayahCount = 206,
            pages = 151..176,
            translation = "The Heights",
          ),
        ),
      ),
      SurahMapping(
        juz = 9,
        page = 162,
        surahs = listOf(
          SuraWithTranslation(
            number = 8,
            revelationOrder = 88,
            isMakki = false,
            nameSimple = "Al-Anfal",
            nameComplex = "Al-'Anfāl",
            nameArabic = "الأنفال",
            ayahCount = 165,
            pages = 177..186,
            translation = "The Spoils of War",
          ),
        ),
      ),
    )
  )
}