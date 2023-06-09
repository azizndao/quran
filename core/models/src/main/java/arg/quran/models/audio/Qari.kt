package arg.quran.models.audio

import androidx.annotation.StringRes

data class Qari(
  val id: Int,
  @StringRes val nameResource: Int,
  val imageUrl: String? = null,
  val url: String,
  val slug: String,
  val db: String? = null
) {
  val databaseName = if (db.isNullOrEmpty()) slug else db
}
