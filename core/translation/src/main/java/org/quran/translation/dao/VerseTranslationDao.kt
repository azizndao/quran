package org.quran.translation.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import arg.quran.models.quran.Verse
import kotlinx.coroutines.flow.Flow
import org.quran.translation.models.TranslatedVerse

@Dao
internal interface VerseTranslationDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAyahs(items: List<TranslatedVerse>)

  @Query("SELECT *, rowid FROM verses WHERE sura = :sura AND ayah = :ayah;")
  suspend fun getAyah(sura: Int, ayah: Int): TranslatedVerse?

  @Query("SELECT *, rowid FROM verses")
  fun getAyahsFlow(): Flow<List<TranslatedVerse>>

  @Query("SELECT *, rowid FROM verses WHERE 1 LIMIT :limit OFFSET :offset")
  fun getAyahsFlow(limit: Int, offset: Int): Flow<List<TranslatedVerse>>

  @Query("SELECT text FROM verses WHERE 1")
  fun getAll(): Flow<List<String>>
}