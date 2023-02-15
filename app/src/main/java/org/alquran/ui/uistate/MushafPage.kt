package org.alquran.ui.uistate

import androidx.compose.ui.text.font.FontFamily
import org.alquran.hafs.model.Word
import org.quram.common.model.VerseKey
import org.quran.network.model.CharType
import org.quran.network.model.WordTranslation

data class MushafPage(
    override val page: Int = 0,
    override val header: Header,
    override val fontFamily: FontFamily,
    val lines: List<Line> = emptyList()
) : QuranPageItem() {

    sealed class Line

    data class ChapterLine(
        val line: Int,
        val sura: Int,
    ) : Line()

    data class TextLine(
        val words: List<LineWord>,
        val line: Int,
    ) : Line()

    data class Basmallah(val line: Int) : Line()

    data class Blank(val line: Int) : Line()

    data class LineWord(
        val id: Int,
        val position: Int,
        val key: VerseKey,
        val audioURL: String? = null,
        val charType: CharType,
        val line: Int,
        val text: String,
        val translation: WordTranslation,
        val transliteration: WordTranslation,
        val playing: Boolean,
        val selected: Boolean
    )
}

fun Word.toLineWord(playing: Boolean, selected: Boolean) = MushafPage.LineWord(
    id = id,
    position = position,
    key = key,
    audioURL = audioURL,
    charType = charType,
    line = line,
    text = text,
    translation = translation,
    transliteration = transliteration,
    playing = playing,
    selected = selected
)