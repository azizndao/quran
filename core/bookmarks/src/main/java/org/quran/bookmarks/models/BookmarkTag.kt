package org.quran.bookmarks.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tags", indices = [Index("name", unique = true)])
data class BookmarkTag(
  @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "tagId") val id: Int = 0,
  val name: String,
  val createdAt: Long = System.currentTimeMillis(),
  val color: Int? = null,
)

