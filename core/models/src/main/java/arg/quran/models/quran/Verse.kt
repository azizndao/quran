package arg.quran.models.quran

import kotlinx.serialization.Serializable

@Serializable
abstract class Verse {

  abstract val sura: Int
  abstract val ayah: Int
  abstract val text: String

  override fun toString(): String {
    return "Verse(sura=$sura, ayah=$ayah, text=$text)"
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Verse

    if (sura != other.sura) return false
    if (ayah != other.ayah) return false
    if (text != other.text) return false

    return true
  }

  override fun hashCode(): Int {
    var result = sura
    result = 31 * result + ayah
    result = 31 * result + text.hashCode()
    return result
  }
}
