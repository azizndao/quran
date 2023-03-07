package org.muslimapp.core.audio.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import arg.quran.models.audio.AudioFile


@Dao
interface AudioFileDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTimings(items: List<AudioFile>)

  @Query("SELECT * FROM audio_files WHERE sura = :sura ORDER BY id ASC")
  suspend fun getTimingBySura(sura: Int): List<AudioFile>
}