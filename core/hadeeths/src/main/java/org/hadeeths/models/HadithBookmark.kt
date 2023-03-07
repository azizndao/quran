package org.hadeeths.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "hadith_bookmarks")
data class HadithBookmark(
  @ColumnInfo(name = "bookmark_id") @PrimaryKey(autoGenerate = true) val id: Int = 0,
  @ColumnInfo(name = "hadith_id") val hadithId: Int,
  @ColumnInfo(name = "created_at") val createdAt: Calendar = Calendar.getInstance(),
)
