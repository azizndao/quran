package org.quran.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.quram.common.model.VerseKey

@Serializable
data class ApiVerse(
    val id: Int,

    @SerialName("verse_key")
    val key: VerseKey,

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

    @SerialName("v2_page")
    val v2Page: Int? = null,

    @SerialName("juz_number")
    val juz: Int,

    val words: List<ApiWord> = emptyList(),
    val translations: List<ApiVerseTranslation> = emptyList(),
)