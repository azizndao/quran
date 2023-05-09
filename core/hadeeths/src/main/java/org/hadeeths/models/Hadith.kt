package org.hadeeths.models

import androidx.room.Entity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The [TranslatedHadith] representation in the locale [org.hadeeths.HadithsDatabase],
 * I need the [language] field to differentiate then the [TranslatedHadith]s in different language will have
 * the same id.
 * The fields [hints], [wordsMeanings] will not have values in none arabic [language]
 */
@Entity(tableName = "hadiths", primaryKeys = ["id", "language"])
data class Hadith(
  val id: Int,
  val title: String,
  val text: String,
  val attribution: String,
  val grade: String,
  val explanation: String,
  val hints: List<String> = emptyList(),
  val categories: List<String>,
  val language: String,
  val otherLanguages: List<String>,
  val wordsMeanings: List<WordsMeaning> = emptyList(),
  val reference: String? = null
)

@Serializable
internal data class ApiHadeeth(
  val id: String,
  val title: String,
  val hadeeth: String,
  val attribution: String,
  val grade: String,
  val explanation: String,
  val hints: List<String> = emptyList(),
  val categories: List<String>,
  val translations: List<String>,

  @SerialName("words_meanings")
  val wordsMeanings: List<WordsMeaning> = emptyList(),

  val reference: String? = null
)

@kotlinx.serialization.Serializable
data class WordsMeaning(
  val word: String,
  val meaning: String
)


internal fun ApiHadeeth.toHadeeth(language: String) = Hadith(
  id = id.toInt(),
  title = title,
  text = hadeeth,
  attribution = attribution,
  grade = grade,
  explanation = explanation,
  hints = hints,
  categories = categories,
  language = language,
  otherLanguages = translations.filter { it != language },
  wordsMeanings = wordsMeanings,
  reference = reference,
)