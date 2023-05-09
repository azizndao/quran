package org.hadeeths.models

import androidx.room.Entity
import kotlinx.serialization.Serializable

/**
 * The [TranslatedHadith] representation in the locale [org.hadeeths.HadithsDatabase],
 * I need the [language] field to differentiate then the [TranslatedHadith]s in different language will have
 * the same id.
 */
@Entity(tableName = "translated_hadiths", primaryKeys = ["id", "language"])
data class TranslatedHadith(
  val id: Int,
  val title: String,
  val text: String,
  val attribution: String,
  val grade: String,
  val explanation: String,
  val categories: List<String>,
  val language: String,
  val otherLanguages: List<String>,
  val reference: String? = null
)

@Serializable
internal data class ApiTranslatedHadeeth(
  val id: String,
  val title: String,
  val hadeeth: String,
  val attribution: String,
  val grade: String,
  val explanation: String,
  val categories: List<String>,
  val translations: List<String>,
  val reference: String? = null
)


internal fun ApiTranslatedHadeeth.toHadeeth(language: String) = TranslatedHadith(
  id = id.toInt(),
  title = title,
  text = hadeeth,
  attribution = attribution,
  grade = grade,
  explanation = explanation,
  categories = categories,
  language = language,
  otherLanguages = translations.filter { it != language }
)