package org.quran.network.verse.model

import kotlinx.serialization.Serializable


@Serializable
internal data class PaginatedResponse<Data>(
  val verses: List<Data>,
  val pagination: Pagination
)
