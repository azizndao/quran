package arg.quran.models.quran

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = SuraAyahSerializer::class)
data class VerseKey(
  val sura: Int,
  val aya: Int,
) : Comparable<VerseKey>, QuranRef.QuranId {

  override fun toString() = "$sura:$aya"

  override fun compareTo(other: VerseKey): Int {
    return when {
      this == other -> 0
      sura != other.sura -> sura.compareTo(other.sura)
      else -> aya.compareTo(other.aya)
    }
  }

  fun after(next: VerseKey): Boolean {
    return this > next
  }

  companion object {
    @JvmStatic
    fun min(a: VerseKey, b: VerseKey): VerseKey {
      return if (a <= b) a else b
    }

    @JvmStatic
    fun max(a: VerseKey, b: VerseKey): VerseKey {
      return if (a >= b) a else b
    }

    fun fromString(value: String): VerseKey? {
      if (value.isEmpty()) return null
      val (surah, ayah) = value.split(":").map { it.toInt() }
      return VerseKey(surah, ayah)
    }
  }
}

class SuraAyahSerializer : KSerializer<VerseKey> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("VerseKey", PrimitiveKind.STRING)

  override fun serialize(encoder: Encoder, value: VerseKey) =
    encoder.encodeString(value.toString())

  override fun deserialize(decoder: Decoder): VerseKey =
    VerseKey.fromString(decoder.decodeString())!!
}
