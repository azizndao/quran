package org.alquran.verses.local.models

import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import arg.quran.models.quran.Verse
import kotlinx.serialization.Serializable

@Fts4
@Serializable
@Entity(tableName = "searchables")
internal data class SearchableVerse(
  @PrimaryKey val rowid: Int = 0,
  override val sura: Int,
  override val ayah: Int,
  override val text: String,
) : Verse()