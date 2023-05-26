package org.quran.bookmarks.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import arg.quran.models.quran.VerseKey

@Entity(tableName = "bookmarks", indices = [Index("key", "tagId", unique = true), Index("tagId")])
data class Bookmark(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val key: VerseKey,
  val label: String,
  val tagId: Int,
  val createdAt: Long = System.currentTimeMillis(),
)
