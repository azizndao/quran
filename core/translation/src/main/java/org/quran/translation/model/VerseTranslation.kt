package org.quran.translation.model

import androidx.room.*
import org.quram.common.model.VerseKey
import org.quran.network.model.ApiVerse

@Fts4
@Entity(tableName = "verses")
data class VerseTranslation(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("rowid") val id: Int = 0,
    val key: VerseKey,
    val text: String,
    val v1Page: Int,
    val v2Page: Int,
    val authorName: String,
)