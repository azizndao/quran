package org.quram.common.utils

interface QuranPageInfo {
    fun juz(page: Int): String
    fun suraName(page: Int): String
    fun displayRub3(page: Int): String
    fun localizedPage(page: Int): String
    fun pageForSuraAyah(sura: Int, ayah: Int): Int
}


internal class QuranPageInfoImpl(
    private val quranInfo: QuranInfo,
    private val quranDisplayData: QuranDisplayData,
    private val rub3DisplayUseCase: Rub3DisplayUseCase,
) : QuranPageInfo {

    override fun juz(page: Int): String {
        return quranDisplayData.getJuzDisplayStringForPage(page)
    }

    override fun suraName(page: Int): String {
        return quranDisplayData.getSuraNameFromPage(page, true)
    }

    override fun displayRub3(page: Int): String {
        return rub3DisplayUseCase(page)
    }

    override fun localizedPage(page: Int): String {
        return QuranUtils.getLocalizedNumber(page)
    }

    override fun pageForSuraAyah(sura: Int, ayah: Int): Int {
        return quranInfo.getPageFromSuraAyah(sura, ayah)
    }
}
