package arg.quran.models.audio

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "audio_files")
@Serializable
data class AudioFile(
  @PrimaryKey val id: Long,

  @SerialName("chapter_id")
  val sura: Long,

  @SerialName("file_size")
  val fileSize: Long,

  @SerialName("audio_url")
  val url: String
)
