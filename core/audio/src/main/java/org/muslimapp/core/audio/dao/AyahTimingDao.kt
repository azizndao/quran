package org.muslimapp.core.audio.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import arg.quran.models.audio.AyaTiming

@Dao
interface AyahTimingDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTimings(timings: List<AyaTiming>)

  @Query("SELECT * FROM timings WHERE sura = :sura ORDER BY aya ASC")
  suspend fun getTimingBySura(sura: Int): List<AyaTiming>
}