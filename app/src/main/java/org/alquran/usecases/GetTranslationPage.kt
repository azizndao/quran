package org.alquran.usecases


import android.content.Context
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import arg.quran.models.Sura
import arg.quran.models.quran.VerseKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import org.alquran.ui.uistate.QuranPageItem
import org.alquran.ui.uistate.TranslationPage
import org.alquran.verses.repository.VerseRepository
import org.muslimapp.core.audio.PlaybackConnection
import org.quram.common.repositories.SurahRepository
import org.quram.common.utils.QuranDisplayData
import org.quram.common.utils.UriProvider
import org.quran.bookmarks.repository.BookmarkRepository
import org.quran.translation.repositories.TranslationRepository

class GetTranslationPage(
  private val verseRepository: VerseRepository,
  private val bookmarkRepository: BookmarkRepository,
  private val surahRepository: SurahRepository,
  private val pageHeaderUseCase: PageHeaderUseCase,
  private val quranDisplayData: QuranDisplayData,
  private val context: Context,
  private val translationRepository: TranslationRepository,
  private val playbackConnection: PlaybackConnection,
) {

  operator fun invoke(page: Int, selectedVerse: VerseKey?, version: Int): Flow<TranslationPage> {
    val keys = quranDisplayData.getAyahKeysOnPage(page)

    var pageHeader: QuranPageItem.Header? = null

    var suras: List<Sura>? = null

    var lastScrollIndex = -1

    return combine(
      verseRepository.getVerses(page),
      translationRepository.getSelectedEditions(page),
      bookmarkRepository.getBookmarksWithKeys(keys),
      playbackConnection.playingAyahFlow,
    ) { verses, translations, bookmarks, playingVerse ->
      if (suras == null) {
        suras = surahRepository.getSurahsInPage(page)
      }

      if (pageHeader == null) {
        pageHeader = pageHeaderUseCase(page)
      }

      val data = mutableListOf<TranslationPage.Row>()

      verses.forEachIndexed { index, verse ->
        val verseKey = VerseKey(verse.sura, verse.ayah)
        if (verseKey.aya == 1) {
          val surah = suras!!.find { it.number == verseKey.sura }!!
          data.add(TranslationPage.Surah(surah.number, surah.nameSimple))
        }

        if (index > 0 && verse.ayah != 1) data.add(TranslationPage.Divider("d$index"))

        val isPlaying = verseKey == playingVerse

        val highLighted = verseKey.aya == selectedVerse?.aya && verseKey.sura == selectedVerse.aya

        val isBookmarked = bookmarks.any {
          verseKey.aya == it.key.aya && it.key.sura == verseKey.sura
        }

        data.add(
          TranslationPage.Verse(
            isPlaying = isPlaying,
            isBookmarked = isBookmarked,
            highLighted = highLighted,
            ayah = verse.ayah,
            sura = verse.sura,
            text = verse.text
          )
        )

        translations.forEach { (edition, verseTranslations) ->
          val translation = verseTranslations.getOrNull(index)
          if (translation != null) {
            data.add(
              TranslationPage.Translation(
                isPlaying = isPlaying,
                isBookmarked = isBookmarked,
                highLighted = highLighted,
                authorName = edition.authorName,
                text = translation.text,
                sura = translation.sura,
                ayah = translation.ayah
              )
            )
          }
        }

        data.add(
          TranslationPage.AyahToolbar(
            verseKey = verseKey,
            isPlaying = isPlaying,
            isBookmarked = isBookmarked,
            highLighted = highLighted
          )
        )
      }

      TranslationPage(
        header = pageHeader!!,
        page = page,
        items = data,
        firstItem = lastScrollIndex == -1,
        scrollIndex = -1,
        fontFamily = FontFamily(Font(UriProvider.getFontFile(context, page, version)))
      )
    }.flowOn(Dispatchers.IO)
  }
}