package org.alquran.ui.screen.pager

import org.quram.common.model.VerseKey


data class QuranPagerArgs(
    val page: Int,
    val verseKey: VerseKey?
)
