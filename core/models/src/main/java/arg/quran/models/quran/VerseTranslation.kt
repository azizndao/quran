package arg.quran.models.quran

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Fts4
@Serializable
@Entity(tableName = "verses")
data class VerseTranslation(
  @SerialName("verse_number")
  @ColumnInfo("rowid")
  @PrimaryKey(autoGenerate = true)
  val id: Int = 0,

  @SerialName("verse_key")
  val key: VerseKey,

  val text: String,

  @SerialName("page_number")
  val page: Int,
)