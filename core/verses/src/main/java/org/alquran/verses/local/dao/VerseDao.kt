package org.alquran.verses.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.alquran.verses.local.models.QuranicVerse

@Dao
internal interface VerseDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(items: List<QuranicVerse>)

  @Query("SELECT * FROM verses;")
  fun getVersesFlow(): Flow<List<QuranicVerse>>

  @Query("SELECT COUNT(*) < 6236 FROM verses")
  suspend fun hasMissingVerses(): Boolean
}