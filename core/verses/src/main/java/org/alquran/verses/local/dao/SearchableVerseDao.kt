package org.alquran.verses.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.alquran.verses.local.models.SearchableVerse

@Dao
internal interface SearchableVerseDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(items: List<SearchableVerse>)

  @Query("SELECT rowid, snippet(searchables, '<b>', '</b>', '...') text, sura, ayah FROM searchables WHERE text MATCH :query")
  suspend fun search(query: String): List<SearchableVerse>
}