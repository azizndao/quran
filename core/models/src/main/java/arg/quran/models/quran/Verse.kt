package arg.quran.models.quran

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "verses", indices = [Index("page")])
data class Verse(
  @PrimaryKey
  val id: Int,

  @SerialName("verse_key")
  @Embedded val key: VerseKey,

  @SerialName("hizb_number")
  val hizb: Int,

  @SerialName("rub_el_hizb_number")
  val rubElHizb: Int,

  @SerialName("ruku_number")
  val ruku: Int,

  @SerialName("manzil_number")
  val manzil: Int,

  @SerialName("sajdah_number")
  val sajdah: Int? = null,

  @SerialName("v1_page")
  val v1Page: Int? = null,

  @SerialName("page_number")
  val page: Int? = null,

  val words: List<VerseWord> = emptyList(),
)
