package org.quran.bookmarks.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.quran.bookmarks.models.BookmarkTag
import org.quran.bookmarks.models.TabWithBookmarks

@Dao
internal interface BookmarkTagDao {

  @Insert(onConflict = OnConflictStrategy.ABORT)
  suspend fun insertOne(tag: BookmarkTag)

  @Transaction
  suspend fun insertAndReturn(tag: BookmarkTag): BookmarkTag {
    insertOne(tag)
    return getName(tag.name)!!
  }

  @Insert(onConflict = OnConflictStrategy.ABORT)
  suspend fun insertMany(tags: List<BookmarkTag>)

  @Query("SELECT * FROM tags;")
  fun observeAll(): Flow<List<BookmarkTag>>

  @Query("SELECT * FROM tags;")
  suspend fun getAll(): List<BookmarkTag>

  @Query("SELECT * FROM tags WHERE tagId = :id;")
  suspend fun getTag(id: Int): BookmarkTag?

  @Query("SELECT * FROM tags WHERE name = :name;")
  suspend fun getName(name: String): BookmarkTag?

  @Query("SELECT * FROM tags WHERE tagId = :id;")
  fun observeById(id: Int): Flow<BookmarkTag?>

  @Query("DELETE FROM tags WHERE tagId = :id")
  suspend fun deleteById(id: Int)

  @Query("SELECT * FROM tags;")
  fun observeTabWithBookmarks(): Flow<List<TabWithBookmarks>>
}