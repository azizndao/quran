package org.quram.common.core

interface QuranPageInfo {
  fun juz(page: Int): String
  fun suraName(page: Int): String
  fun displayRub3(page: Int): String
  fun localizedPage(page: Int): String
  fun pageForSuraAyah(sura: Int, ayah: Int): Int
}
