package org.alquran.verses.local.models

import androidx.room.Entity
import androidx.room.Index
import arg.quran.models.quran.Verse
import kotlinx.serialization.Serializable

@Entity(
  tableName = "verses",
  primaryKeys = ["sura", "ayah"],
  indices = [Index("ayah", "sura", name = "sura_index")]
)
@Serializable
internal data class QuranicVerse(
  override val sura: Int,
  override val ayah: Int,
  override val text: String,
) : Verse()
