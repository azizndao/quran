package org.quran.qari.model

import android.content.Context
import android.os.Parcelable
import arg.quran.models.audio.Qari
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class QariItem(
  val id: Int,
  val name: String,
  val url: String,
  val path: String,
  val db: String? = null
) : Parcelable {
  @IgnoredOnParcel
  val databaseName = if (db.isNullOrEmpty()) null else db

  val isGapless: Boolean
    get() = databaseName != null

  companion object {
    fun fromQari(context: Context, qari: Qari): QariItem {
      return org.quran.qari.model.QariItem(
        id = qari.id,
        name = context.getString(qari.nameResource),
        url = qari.url,
        path = qari.path,
        db = qari.db
      )
    }
  }
}
