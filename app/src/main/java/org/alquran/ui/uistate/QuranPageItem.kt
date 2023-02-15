package org.alquran.ui.uistate

import androidx.compose.ui.text.font.FontFamily

sealed class QuranPageItem {
    abstract val page: Int
    abstract val header: Header
    abstract val fontFamily: FontFamily

    data class Header(val leading: String, val trailing: String)
}

