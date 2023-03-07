package org.quram.common.utils

import android.content.Context
import java.io.File

object UriProvider {

  fun getPatchUrl() =
    "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/data/versions.json"

  fun getHafsUrl(version: Int): String =
    "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/data/quran/hafs.v${version}.zip"

  fun getTimingData(slug: String): String =
    "https://zesfefssvwcigbnloyno.supabase.co/storage/v1/object/public/data/timings/$slug.json"

  fun getAssetFontPath(page: Int, version: Int): String = "fonts/v$version/p$page.ttf"

  fun getFontFile(context: Context, page: Int, version: Int): File {
    return File(getFontFolder(context, version), "p$page.ttf")
  }

  fun getFontRootFolder(context: Context): File {
    val dir = context.getExternalFilesDir("Fonts")!!
    if (!dir.exists()) dir.mkdirs()
    return dir
  }

  fun getFontFolder(context: Context, version: Int): File {
    val dir = File(getFontRootFolder(context), "v$version")
    if (!dir.exists()) dir.mkdirs()
    return dir
  }
}