package org.alquran.ui.uistate

import androidx.compose.ui.text.font.FontFamily
import org.alquran.audio.models.AudioState
import org.quram.common.model.VerseKey

data class TranslationPage(
    override val page: Int = 0,
    override val header: Header,
    val items: List<Row> = emptyList(),
    val scrollIndex: Int,
    val firstItem: Boolean,
    override val fontFamily: FontFamily,
) : QuranPageItem() {
    sealed class Row(open val key: Any)

    data class Surah(val number: Int, val name: String) : Row(number)

    data class Verse(
        val verse: org.alquran.hafs.model.Verse,
        val isBookmarked: Boolean,
        val highLighted: Boolean,
        val audioState: AudioState,
    ) : Row("Verse(${verse.key})")

    data class Translation(
        val translation: org.alquran.hafs.model.VerseTranslation,
        val audioState: AudioState,
        val isBookmarked: Boolean,
        val highLighted: Boolean
    ) : Row("Translation(${translation.id})")

    data class AyahToolbar(
        val verseKey: VerseKey,
        val isBookmarked: Boolean,
        val highLighted: Boolean,
        val audioState: AudioState,
    ) : Row("AyahToolbar($verseKey)")

    class Divider(key: Any) : Row(key)

}

fun List<TranslationPage.Row>.indexOfItem(sura: Int, aya: Int): Int {
    val index = indexOfFirst { model ->
        when (model) {
            is TranslationPage.Surah -> model.number == sura && aya == 1
            is TranslationPage.Verse -> model.verse.key.ayah == aya && model.verse.key.sura == sura
            else -> false
        }
    }
    return if (index > 0) index + 1 else index
}
