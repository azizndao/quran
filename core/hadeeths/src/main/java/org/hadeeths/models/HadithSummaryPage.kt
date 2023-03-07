package org.hadeeths.models

import kotlinx.serialization.SerialName

@Serializable
data class HadithSummaryPage(
  val data: List<ApiHadeethSummary>,
  val meta: Meta
) {

  @Serializable
  data class Meta(
    @SerialName("current_page") val currentPage: Int,
    @SerialName("last_page") val lastPage: Int,
    @SerialName("per_page") val perPage: Int,
    @SerialName("total_items") val totalItems: Int
  )
}