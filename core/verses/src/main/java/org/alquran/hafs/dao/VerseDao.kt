package org.alquran.hafs.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import arg.quran.models.quran.Verse
import kotlinx.coroutines.flow.Flow

@Dao
interface VerseDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertVerses(items: List<Verse>)

  @Query("SELECT * FROM verses WHERE page = :page")
  fun getVersesFlow(page: Int): Flow<List<Verse>>

  @Query("SELECT COUNT(*) < 6236 FROM verses")
  suspend fun hasMissingVerses(): Boolean
}