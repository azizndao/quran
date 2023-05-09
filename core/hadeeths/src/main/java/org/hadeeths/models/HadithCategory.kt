package org.hadeeths.models

import androidx.room.Entity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * [HadithCategory] can be root category in that case it won't have [parentId] or inner category
 * I decide to combine [id] and [language] as primary keys because the [id] won't change when you
 * decide to change the [language] from the preferences but the combination is unique
 *
 */
@Serializable
@Entity(tableName = "categories", primaryKeys = ["id", "language"])
data class HadithCategory(
  val id: Int,
  val name: String,
  val hadithsCount: Int,
  val parentId: Int? = null,
  val language: String,
)

/**
 * The category from the api
 */
@Serializable
internal data class ApiHadithCategory(
  val id: String,
  val title: String,
  @SerialName("hadeeths_count") val hadithsCount: Int,
  @SerialName("parent_id") val parentId: Int? = null,
)

internal fun ApiHadithCategory.toHadithCategory(language: String) = HadithCategory(
  id = id.toInt(),
  name = title,
  hadithsCount = hadithsCount,
  parentId = parentId,
  language = language
)