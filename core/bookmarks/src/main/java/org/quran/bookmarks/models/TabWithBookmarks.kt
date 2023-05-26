package org.quran.bookmarks.models

import androidx.room.Embedded
import androidx.room.Relation

data class TabWithBookmarks(
  @Embedded val tag: BookmarkTag,

  @Relation(
    parentColumn = "tagId",
    entityColumn = "tagId"
  )
  val bookmarks: List<Bookmark>
)
