package org.quran.translation.local.models

import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import arg.quran.models.quran.Verse

@Fts4
@Entity(tableName = "verses")
internal data class TranslatedVerse(
  @PrimaryKey val rowid: Int = 0,
  override val sura: Int,
  override val ayah: Int,
  override val text: String,
) : Verse()