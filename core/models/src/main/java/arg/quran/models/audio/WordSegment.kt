package arg.quran.models.audio

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WordSegment(
  @SerialName("word") val position: Int,
  val sura: Int,
  val aya: Int,
  val startDuration: Long,
  val endDuration: Long,
)