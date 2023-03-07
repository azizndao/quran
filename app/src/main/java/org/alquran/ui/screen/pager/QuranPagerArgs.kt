package org.alquran.ui.screen.pager

import arg.quran.models.quran.VerseKey


data class QuranPagerArgs(
  val page: Int,
  val verseKey: VerseKey?
)
