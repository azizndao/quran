package org.alquran.hafs.model

import kotlinx.serialization.Serializable
import org.quram.common.model.VerseKey
import org.quran.network.model.ApiWord
import org.quran.network.model.CharType
import org.quran.network.model.WordTranslation

@Serializable
data class Word(
    val id: Int,
    val position: Int,
    val key: VerseKey,
    val audioURL: String? = null,
    val charType: CharType,
    val line: Int,
    val text: String,
    val translation: WordTranslation,
    val transliteration: WordTranslation
)

internal fun ApiWord.toDbModel(): Word {
    return Word(
        id = id,
        position = position,
        key = key,
        audioURL = audioURL,
        charType = charType,
        line = line,
        text = codeV1 ?: codeV2!!,
        translation = translation,
        transliteration = transliteration
    )
}