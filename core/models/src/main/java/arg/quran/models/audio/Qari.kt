package arg.quran.models.audio

import androidx.annotation.StringRes

data class Qari(
  val id: Int,
  @StringRes val nameResource: Int,
  val imageUrl: String? = null,
  val url: String,
  val path: String,
  val db: String? = null
) {
  val databaseName = if (db.isNullOrEmpty()) path else db
  val isGapless: Boolean = true
}
