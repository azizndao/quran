package org.quran.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pagination(
    @SerialName("per_page")
    val perPage: Int,

    @SerialName("current_page")
    val currentPage: Int,

    @SerialName("next_page")
    val nextPage: Int? = null,

    @SerialName("total_pages")
    val totalPages: Int,

    @SerialName("total_records")
    val totalRecords: Int
)
