package org.alquran.verses.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import arg.quran.models.quran.Verse
import org.alquran.verses.models.SearchableVerse

@Dao
internal interface SearchableVerseDao {

  @Insert
  suspend fun insert(items: List<SearchableVerse>)

  @Query("SELECT *, rowid FROM searchables WHERE text MATCH :query")
  suspend fun search(query: String): List<SearchableVerse>
}