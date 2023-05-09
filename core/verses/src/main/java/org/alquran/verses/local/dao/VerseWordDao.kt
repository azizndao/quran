package org.alquran.verses.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import arg.quran.models.quran.QuranWord
import kotlinx.coroutines.flow.Flow

@Dao
internal interface VerseWordDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(items: List<QuranWord>)

  @Query("SELECT * FROM words WHERE page = :page")
  fun getAllWords(page: Int): Flow<List<QuranWord>>
}