package org.alquran.hafs.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import org.quram.common.model.VerseKey
import org.quran.network.model.ApiVerseTranslation

@Fts4
@Entity(tableName = "verse_translations")
data class VerseTranslation(
    @ColumnInfo("rowid") @PrimaryKey val id: Int,
    val authorId: Int,
    val authorName: String,
    val key: VerseKey,
    val text: String,
)

 fun ApiVerseTranslation.toDbModel() = VerseTranslation(id, authorId, authorName, key, text)