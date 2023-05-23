package org.quran.core.audio.models

import android.content.Context
import android.os.Parcelable
import arg.quran.models.audio.Qari
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class QariItem(
  val id: Int,
  val imageUri: String? = null,
  val name: String,
  val url: String,
  val path: String,
  val db: String? = null
) : Parcelable {

  @IgnoredOnParcel
  val databaseName = if (db.isNullOrEmpty()) path else db

  @IgnoredOnParcel
  val isGapless: Boolean = true

  companion object {
    fun fromQari(context: Context, qari: Qari): QariItem {
      return QariItem(
        id = qari.id,
        name = context.getString(qari.nameResource),
        url = qari.url,
        path = qari.path,
        db = qari.db,
        imageUri = qari.imageUrl
      )
    }
  }
}
