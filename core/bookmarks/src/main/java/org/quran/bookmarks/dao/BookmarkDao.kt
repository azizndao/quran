package org.quran.bookmarks.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.quram.common.model.VerseKey
import org.quran.bookmarks.model.Bookmark

@Dao
internal interface BookmarkDao {

    @Insert
    suspend fun insertBookmarks(vararg items: Bookmark)

    @Query("SELECT * FROM bookmarks ORDER BY createdAt DESC")
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    fun getAll(): Flow<List<Bookmark>>

    @Update
    suspend fun update(bookmark: Bookmark)

    @Delete
    suspend fun delete(bookmark: Bookmark)

    @Query("DELETE FROM bookmarks WHERE `key` = :key")
    suspend fun delete(key: VerseKey)

    @Query("SELECT * FROM bookmarks WHERE `key` = :key")
    suspend fun getBookmark(key: VerseKey): Bookmark?

    @Query("SELECT * FROM bookmarks WHERE `key` in (:keys)")
    fun getByPage(keys: Set<VerseKey>): Flow<List<Bookmark>>
}