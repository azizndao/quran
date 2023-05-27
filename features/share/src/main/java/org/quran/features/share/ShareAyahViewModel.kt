package org.quran.features.share

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import arg.quran.models.quran.VerseKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.alquran.verses.repository.VerseRepository
import org.quram.common.core.QuranInfo
import org.quram.common.repositories.SurahRepository
import org.quran.features.share.navigation.shareAyahDestinationArgs
import org.quran.features.share.views.ShareCardView
import org.quran.translation.repositories.TranslationsRepository
import org.quran.translation.repositories.VerseTranslationRepository
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


internal class ShareAyahViewModel(
  savedStateHandle: SavedStateHandle,
  private val ayahRepository: VerseRepository,
  private val surahRepository: SurahRepository,
  private val translationsRepository: TranslationsRepository,
  private val verseTranslationRepository: VerseTranslationRepository,
  private val quranInfo: QuranInfo,
  app: Application
) : AndroidViewModel(app) {

  private val suraAyah: VerseKey = shareAyahDestinationArgs(savedStateHandle)

  var uiState: ShareAyahUiState by mutableStateOf(ShareAyahUiState.Loading)

  init {
    viewModelScope.launch {
      val ayah = ayahRepository.get(suraAyah)!!

      val translation = withContext(Dispatchers.IO) {
        translationsRepository.getSelectedTranslationSlugs()
          .firstOrNull { it != "transliteration" }
          ?.let {
            verseTranslationRepository.getAyahTranslation(
              suraAyah.sura,
              suraAyah.aya,
              it
            )
          }
      }

      val suraName = surahRepository.getSurahName(suraAyah.sura - 1)

      uiState = ShareAyahUiState.Success(
        ayah = ayah.ayah,
        sura = ayah.sura,
        surahName = suraName,
        text = ayah.text,
        translation = translation?.text,
        page = quranInfo.getPageFromSuraAyah(suraAyah.sura, suraAyah.aya)
      )
    }
  }

  fun share(card: ShareCardView) {
    val bitmap = generateBitmap(card)

    if (uiState !is ShareAyahUiState.Success) return

    val data = uiState as ShareAyahUiState.Success

    shareImageToOthers(
      card.context,
      "${data.surahName} ${data.sura}:${data.ayah}",
      bitmap
    )
  }

  private fun generateBitmap(view: View): Bitmap {
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.layout(view.left, view.top, view.right, view.bottom)
    view.draw(canvas)
    return bitmap
  }

  private fun saveBitmapAndGetUri(context: Context, bitmap: Bitmap): Uri? {
    val path: String =
      context.externalCacheDir.toString() + "/share_${System.currentTimeMillis()}.jpg"
    val out: OutputStream
    val file = File(path)
    try {
      out = FileOutputStream(file)
      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
      out.flush()
      out.close()
    } catch (e: Exception) {
      e.printStackTrace()
    }
    return FileProvider.getUriForFile(
      context, context.packageName + ".provider", file
    )
  }

  private fun shareImageToOthers(context: Context, text: String?, bitmap: Bitmap?) {
    val imageUri: Uri? = bitmap?.let { saveBitmapAndGetUri(context, it) }
    val chooserIntent = Intent(Intent.ACTION_SEND)
    chooserIntent.type = "image/*"
    chooserIntent.putExtra(Intent.EXTRA_TEXT, text)
    chooserIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
    chooserIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    if (chooserIntent.resolveActivity(context.packageManager) != null) {
      context.startActivity(chooserIntent)
    }
  }
}
