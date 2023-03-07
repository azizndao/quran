package org.alquran.usecases

import org.alquran.ui.uistate.QuranPageItem
import org.quram.common.utils.QuranDisplayData
import org.quram.common.utils.Rub3DisplayUseCase

class PageHeaderUseCase(
  private val quranDisplayData: QuranDisplayData,
  private val rub3DisplayUseCase: Rub3DisplayUseCase,
) {

  operator fun invoke(page: Int): QuranPageItem.Header {
    val juz = quranDisplayData.getJuzDisplayStringForPage(page)
    quranDisplayData.getSuraNameFromPage(page)
    return QuranPageItem.Header(
      leading = quranDisplayData.getSuraNameFromPage(page),
      trailing = juz + rub3DisplayUseCase(page)
    )
  }
}