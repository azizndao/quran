package org.quran.datastore

data class QuranPosition(
    val sura: Int,
    val ayah: Int,
    val page: Int,
) {

    override fun toString() = "$sura:$ayah:$page"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuranPosition

        if (sura != other.sura) return false
        if (ayah != other.ayah) return false
        if (page != other.page) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sura
        result = 31 * result + ayah
        result = 31 * result + page
        return result
    }

    companion object {
        val First: QuranPosition get() = QuranPosition(sura = 1, page = 1, ayah = 1)

        fun fromString(value: String): QuranPosition {
            val values = value.split(":").map(String::toInt)
            return QuranPosition(values[0], values[1], values[2])
        }
    }
}