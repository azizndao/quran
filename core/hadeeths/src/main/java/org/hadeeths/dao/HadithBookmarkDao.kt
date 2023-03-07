package org.hadeeths.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.hadeeths.models.HadithBookmark

@Dao
interface HadithBookmarkDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(vararg items: HadithBookmark)

  @Query("SELECT * FROM hadith_bookmarks WHERE hadith_id = :hadithId")
  fun getByHadithId(hadithId: Int): Flow<HadithBookmark?>

  @Query("SELECT * FROM hadith_bookmarks WHERE hadith_id = (:hadithIds)")
  suspend fun getByHadithIds(hadithIds: Set<Int>): List<HadithBookmark>

  @Delete
  suspend fun remove(bookmark: HadithBookmark)

  @Query("DELETE FROM hadith_bookmarks WHERE hadith_id = :hadithId")
  suspend fun remove(hadithId: Int)
}