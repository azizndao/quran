package arg.quran.models.quran

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerseWord(
  val id: Int,
  val position: Int,

  @SerialName("verse_key")
  val key: VerseKey,

  @SerialName("audio_url")
  val audioURL: String? = null,

  @SerialName("char_type_name")
  val charType: CharType,

  @SerialName("line_number")
  val line: Int,

  @SerialName("code_v1")
  val codeV1: String? = null,

  @SerialName("code_v2")
  val codeV2: String? = null,

  val translation: WordTranslation,

  val transliteration: WordTranslation
)


@Serializable
enum class CharType(val value: String) {
  @SerialName("end")
  End("end"),

  @SerialName("word")
  Word("word");
}

@Serializable
data class WordTranslation(
  val text: String? = null,
  @SerialName("language_name")
  val languageName: String
)
