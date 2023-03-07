package org.muslimapp.core.audio.models

import androidx.room.Entity
import kotlinx.serialization.Serializable


@Serializable
@Entity(tableName = "timings", primaryKeys = ["sura", "ayah"])
data class AyahTiming(
  val sura: Int,
  val ayah: Int,
  val time: Long,
)
