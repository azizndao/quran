package org.quran.bookmarks.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import arg.quran.models.quran.VerseKey
import java.util.*

@Entity(tableName = "bookmarks")
data class Bookmark(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val key: VerseKey,
  val name: String,
  val tag: BookmarkTag = BookmarkTag.Bookmark,
  val createdAt: Calendar = Calendar.getInstance(),
)
