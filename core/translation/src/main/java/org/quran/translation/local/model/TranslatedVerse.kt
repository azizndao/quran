package org.quran.translation.local.model

import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import arg.quran.models.quran.Verse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Fts4
@Serializable
@Entity(tableName = "translations")
data class TranslatedVerse(
  @PrimaryKey(autoGenerate = true) val rowid: Int = 0,
  @SerialName("chapter_id") override val sura: Int,
  @SerialName("verse_number") override val ayah: Int,
  override val text: String
): Verse()
