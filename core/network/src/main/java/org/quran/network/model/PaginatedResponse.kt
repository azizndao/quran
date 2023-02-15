package org.quran.network.model

import kotlinx.serialization.Serializable


@Serializable
data class PaginatedResponse <Data>(
    val verses: List<Data>,
    val pagination: Pagination
)
