package org.quram.common.source

import arg.quran.models.Juz
import arg.quran.models.quran.VerseKey
import org.quram.common.datasources.JuzsArray


interface QuranDataSource {
  val numberOfPages: Int
  val pageForSuraArray: IntArray
  val suraForPageArray: IntArray
  val ayahForPageArray: IntArray
  val pageForJuzArray: IntArray
  val juzDisplayPageArrayOverride: Map<Int, Int>
  val numberOfAyahsForSuraArray: IntArray
  val isMakkiBySuraArray: BooleanArray
  val quarterStartByPage: IntArray
  val quartersArray: Array<VerseKey>

  val juzsArray: Array<Juz> get() = JuzsArray
}
