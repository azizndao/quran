package org.muslimapp.core.audio.models

import androidx.room.*
import kotlinx.serialization.Serializable


@Serializable
@Entity(tableName = "timings", primaryKeys = ["sura", "ayah"])
data class AyahTiming(
  val sura: Int,
  val ayah: Int,
  val time: Long,
)


@Dao
interface AyahTimingDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTimings(timings: List<AyahTiming>)

  @Query("SELECT * FROM timings WHERE sura = :sura ORDER BY ayah ASC")
  suspend fun getTimingBySura(sura: Int): List<AyahTiming>
}