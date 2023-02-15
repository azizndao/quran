package org.alquran.hafs.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.alquran.hafs.model.Verse
import org.alquran.hafs.model.VerseTranslation
import org.alquran.hafs.model.VerseWithTranslation

@Dao
interface VerseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerses(items: List<Verse>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslation(items: List<VerseTranslation>)

    @Query("SELECT * FROM verses WHERE page = :page")
    fun getVersesFlow(page: Int): Flow<List<Verse>>

    @Query("SELECT * FROM verses WHERE page = :page")
    fun getVersesWithTranslations(page: Int): Flow<List<VerseWithTranslation>>

    @Query("SELECT COUNT(*) < 6236 FROM verses")
    suspend fun hasMissingVerses(): Boolean
}