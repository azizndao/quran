package org.quram.common.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.quram.common.utils.QuranConstants
import org.quram.common.utils.QuranInfo

@kotlinx.serialization.Serializable(with = SuraAyahSerializer::class)
data class VerseKey(
    val sura: Int,
    val ayah: Int,
) : Comparable<VerseKey>, QuranRef.QuranId {

    override fun toString() = "$sura:$ayah"

    override fun compareTo(other: VerseKey): Int {
        return when {
            this == other -> 0
            sura != other.sura -> sura.compareTo(other.sura)
            else -> ayah.compareTo(other.ayah)
        }
    }

    fun after(next: VerseKey): Boolean {
        return this > next
    }

    fun next(quranInfo: QuranInfo): VerseKey? {
        return if (ayah < quranInfo.getNumberOfAyahs(sura)) {
            VerseKey(sura, ayah + 1)
        } else if (sura < QuranConstants.NUMBER_OF_SURAS) {
            VerseKey(sura + 1, 1)
        } else {
            null
        }
    }

    fun prev(quranInfo: QuranInfo): VerseKey? {
        return if (ayah > 1) {
            VerseKey(sura, ayah - 1)
        } else if (sura > 1) {
            VerseKey(sura - 1, quranInfo.getNumberOfAyahs(sura - 1))
        } else {
            null
        }
    }

    fun id(quranInfo: QuranInfo): Int {
        return quranInfo.getAyahId(sura, ayah)
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
