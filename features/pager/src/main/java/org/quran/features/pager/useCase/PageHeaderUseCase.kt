package org.quran.features.pager.useCase

import org.quram.common.utils.QuranDisplayData
import org.quram.common.utils.Rub3DisplayUseCase
import org.quran.features.pager.uiState.PageItem

class PageHeaderUseCase(
  private val quranDisplayData: QuranDisplayData,
  private val rub3DisplayUseCase: Rub3DisplayUseCase,
) {

  operator fun invoke(page: Int): PageItem.Header {
    val juz = quranDisplayData.getJuzDisplayStringForPage(page)
    quranDisplayData.getSuraNameFromPage(page)
    return PageItem.Header(
      leading = quranDisplayData.getSuraNameFromPage(page),
      trailing = juz + rub3DisplayUseCase(page)
    )
  }
}