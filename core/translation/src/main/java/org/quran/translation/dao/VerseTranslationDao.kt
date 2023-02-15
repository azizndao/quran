package org.quran.translation.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.quram.common.model.VerseKey
import org.quran.translation.model.VerseTranslation

@Dao
internal interface VerseTranslationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAyahs(items: List<VerseTranslation>)

    @Query("SELECT *, rowid FROM verses WHERE `key` = :key")
    suspend fun getAyah(key: VerseKey): VerseTranslation?

    @Query("SELECT *, rowid FROM verses")
    fun getAyahsFlow(): Flow<List<VerseTranslation>>

    @Query("SELECT *, rowid FROM verses WHERE 1 LIMIT :limit OFFSET :offset")
    fun getAyahsFlow(limit: Int, offset: Int): Flow<List<VerseTranslation>>

    @Query("SELECT text FROM verses WHERE 1")
    fun getAll(): Flow<List<String>>

    @Query("SELECT *, rowid FROM verses WHERE v1page = :page")
    fun getByV1Page(page: Int): Flow<List<VerseTranslation>>

    @Query("SELECT *, rowid FROM verses WHERE v2page = :page")
    fun getByV2Page(page: Int): Flow<List<VerseTranslation>>
}