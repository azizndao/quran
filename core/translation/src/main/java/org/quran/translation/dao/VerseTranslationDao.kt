package org.quran.translation.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import arg.quran.models.quran.VerseKey
import arg.quran.models.quran.VerseTranslation
import kotlinx.coroutines.flow.Flow

@Dao
internal interface VerseTranslationDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAyahs(items: List<VerseTranslation>)

  @Query("SELECT *, rowid FROM verses WHERE `key` = :key;")
  suspend fun getAyah(key: VerseKey): VerseTranslation?

  @Query("SELECT *, rowid FROM verses")
  fun getAyahsFlow(): Flow<List<VerseTranslation>>

  @Query("SELECT *, rowid FROM verses WHERE 1 LIMIT :limit OFFSET :offset")
  fun getAyahsFlow(limit: Int, offset: Int): Flow<List<VerseTranslation>>

  @Query("SELECT text FROM verses WHERE 1")
  fun getAll(): Flow<List<String>>

  @Query("SELECT *, rowid FROM verses WHERE page = :page")
  fun getByPage(page: Int): Flow<List<VerseTranslation>>
}