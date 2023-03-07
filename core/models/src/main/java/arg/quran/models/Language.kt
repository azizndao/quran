package arg.quran.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity("languages")
@Serializable
data class Language(
  @PrimaryKey val id: Long,

  val name: String,

  @SerialName("iso_code")
  val isoCode: String,

  @SerialName("native_name")
  val nativeName: String,

  val direction: Direction,

  @SerialName("translations_count")
  val translationsCount: Long,
)
