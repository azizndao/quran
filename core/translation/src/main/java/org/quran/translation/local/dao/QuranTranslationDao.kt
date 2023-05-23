package org.quran.translation.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.quran.translation.local.model.TranslatedVerse

@Dao
internal interface QuranTranslationDao {

  @Transaction
  suspend fun clearAndInsertAll(items: List<TranslatedVerse>) {
    cleanAll()
    insertAyahs(items)
  }

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAyahs(items: List<TranslatedVerse>)

  @Query("SELECT *, rowid FROM translations WHERE sura = :sura AND ayah = :ayah")
  suspend fun getAyah(sura: Int, ayah: Int): TranslatedVerse?

  @Query("SELECT *, rowid FROM translations")
  fun observeAllVerses(): Flow<List<TranslatedVerse>>

  @Query("SELECT *, rowid FROM translations WHERE 1 LIMIT :limit OFFSET :offset")
  fun observeAllVerses(limit: Int, offset: Int): Flow<List<TranslatedVerse>>

  @Query("SELECT text FROM translations WHERE 1 ORDER by rowid")
  fun observeAll(): Flow<List<String>>

  @Query("SELECT *, snippet(translations, '<b>', '</b>', '...') text, rowid  FROM translations WHERE text MATCH :query")
  suspend fun search(query: String): List<TranslatedVerse>

  @Query("DELETE FROM translations")
  suspend fun cleanAll()
}