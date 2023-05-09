package org.hadeeths.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.hadeeths.models.*

@Dao
interface HadithDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertHadeeths(items: Hadith)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTranslatedHadeeths(items: List<TranslatedHadith>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSummaries(items: List<HadithSummaryEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCategories(items: List<HadithCategory>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertLanguage(items: List<HadithLanguage>)

  @Query("SELECT * FROM categories WHERE language = :language")
  fun getAllCategories(language: String): Flow<List<HadithCategory>>

  @Query(
    """
        SELECT *, s.rowid id FROM summaries s
        LEFT JOIN hadith_bookmarks b ON b.hadith_id = s.rowid
        WHERE categoryId = :categoryId AND language = :language
    """
  )
  fun getHadeethSummaries(categoryId: Int, language: String): Flow<List<HadithSummary>>

  @Query("SELECT * FROM hadiths WHERE id = :id")
  suspend fun getHadeeth(id: Int): Hadith?

  @Query("SELECT * FROM translated_hadiths WHERE id = :id AND language = :language")
  suspend fun getTranslatedHadeeths(id: Int, language: String): TranslatedHadith?

  @Query("SELECT * FROM languages WHERE 1")
  fun getAllLanguages(): Flow<List<HadithLanguage>>

  @Query("SELECT * FROM languages WHERE isoCode = :code")
  suspend fun getLanguage(code: String): HadithLanguage?

  @Query(
    """
      SELECT *, s.rowid id FROM summaries s
      JOIN hadith_bookmarks b ON b.hadith_id = s.rowid
      ORDER BY b.created_at DESC
    """
  )
  fun getBookmarkedHadiths(): Flow<List<HadithSummary>>

  @Query(
    """
      SELECT s.rowid id, s.categoryId, s.language, c.name categoryName, snippet(summaries, '<b>', '</b>', '...') snippet 
      FROM summaries s
      JOIN categories c ON s.categoryId = c.id AND c.language = s.language
      WHERE title MATCH :query AND s.language = :language
    """
  )
  suspend fun search(query: String, language: String): List<HadithResult>

  @Query(
    """
      SELECT s.rowid id, s.categoryId, s.language, c.name categoryName, snippet(summaries, '<b>', '</b>', '...') snippet 
      FROM summaries s
      JOIN categories c ON s.categoryId = c.id AND s.categoryId = :categoryId AND c.language = s.language
      WHERE title MATCH :query AND s.language = :language
    """
  )
  suspend fun search(query: String, language: String, categoryId: Int): List<HadithResult>
}