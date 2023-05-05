package arg.quran.models.audio

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import arg.quran.models.quran.VerseKey
import kotlinx.serialization.SerialName

@Entity(tableName = "timings", )
data class AyaTiming(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,

  @SerialName("verse_key")
  @Embedded val verseKey: VerseKey,

  val url: String,
  val segments: List<WordSegment>,
  val duration: Long
)

val AyaTiming.sura get() = verseKey.sura

val AyaTiming.aya get() = verseKey.aya