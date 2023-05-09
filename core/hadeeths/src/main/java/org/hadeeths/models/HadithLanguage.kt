package org.hadeeths.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "languages")
@Serializable
data class HadithLanguage(
  @SerialName("code") @PrimaryKey val isoCode: String,
  @SerialName("native") val nativeName: String,
)
