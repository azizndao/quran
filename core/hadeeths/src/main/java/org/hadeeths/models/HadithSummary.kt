package org.hadeeths.models

import androidx.room.*
import kotlinx.serialization.Serializable

/**
 * The portion of the hadeeth that will be show on the corresponding [HadithCategory] section.
 */
data class HadithSummary(
  val id: Int,
  val title: String,
  val otherLanguage: List<String>,
  val language: String,
  val categoryId: Int,
  @Embedded val bookmark: HadithBookmark? = null,
)

/**
 * I decide to separate [Hadith] to [HadithSummaryEntity] because the [Fts4] search is apply only to
 * the [title].
 */
@Fts4
@Entity(tableName = "summaries")
data class HadithSummaryEntity(
  @PrimaryKey @ColumnInfo(name = "rowid") val id: Int,
  val title: String,
  val otherLanguage: List<String>,
  val language: String,
  val categoryId: Int,
)

@Serializable
data class ApiHadeethSummary(
  val id: String,
  val title: String,
  val translations: List<String>,
)

internal fun ApiHadeethSummary.toEntity(language: String, categoryId: Int) =
  HadithSummaryEntity(
    id = id.toInt(),
    title = title,
    language = language,
    otherLanguage = translations.filter { it != language },
    categoryId = categoryId,
  )

internal fun HadithSummaryEntity.toUiModel(bookmark: HadithBookmark? = null) = HadithSummary(
  id = id,
  title = title,
  language = language,
  otherLanguage = otherLanguage,
  categoryId = categoryId,
  bookmark = bookmark,
)