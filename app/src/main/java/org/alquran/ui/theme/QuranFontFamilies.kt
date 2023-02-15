package org.alquran.ui.theme

import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import kotlinx.coroutines.CoroutineExceptionHandler
import org.alquran.R
import timber.log.Timber

object QuranFontFamilies {
    val SuraNames by lazy { FontFamily(Font(R.font.sura_names)) }

    val Hafs by lazy { FontFamily(Font(R.font.hafs_smart_08)) }

    val UthmanTahaNaskh by lazy {
        FontFamily(
            Font(R.font.uthman_taha_naskh_v2_0),
            Font(R.font.uthman_taha_naskh_v2_0, weight = FontWeight.Bold)
        )
    }

    val WorkSans by lazy {
        FontFamily(Font(R.font.worksans_bold))
    }

    val handler = CoroutineExceptionHandler { _, throwable ->
        Timber.tag("MuslimsFontFamilies").e(throwable)
    }
}
