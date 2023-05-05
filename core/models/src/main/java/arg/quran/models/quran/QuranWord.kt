package arg.quran.models.quran

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(
  tableName = "words",
  indices = [
    Index("key", name = "key_index"),
    Index("line", name = "line_index"),
    Index("page", name = "page_index"),
  ]
)
data class QuranWord(
  @PrimaryKey val id: Int,
  val position: Int,
  val key: VerseKey,
  val type: CharType,
  val line: Int,
  val page: Int,
  val text: String,
)

@Serializable
enum class CharType(val value: String) {
  @SerialName("end")
  End("end"),

  @SerialName("word")
  Word("word");
}
