package org.alquran.usecases


import android.content.Context
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import arg.quran.models.Sura
import arg.quran.models.quran.VerseKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge
import org.alquran.audio.models.AudioState
import org.alquran.hafs.repository.VerseRepository
import org.alquran.ui.uistate.QuranPageItem
import org.alquran.ui.uistate.TranslationPage
import org.alquran.ui.uistate.toUiState
import org.quram.common.repositories.SurahRepository
import org.quram.common.utils.QuranDisplayData
import org.quram.common.utils.UriProvider
import org.quran.bookmarks.repository.BookmarkRepository
import org.quran.translation.repositories.TranslationAndVerses
import org.quran.translation.repositories.TranslationRepository

class GetTranslationPage(
  private val verseRepository: VerseRepository,
  private val bookmarkRepository: BookmarkRepository,
  private val surahRepository: SurahRepository,
  private val pageHeaderUseCase: PageHeaderUseCase,
  private val quranDisplayData: QuranDisplayData,
  private val context: Context,
  private val translationRepository: TranslationRepository
) {

  private fun getTranslation(page: Int): Flow<List<TranslationAndVerses>> {
    return translationRepository.getSelectedTranslations().flatMapMerge { editions ->
      translationRepository.getQuranTranslations(
        editions,
        page,
      )
    }
  }

  operator fun invoke(
    page: Int,
    selectedVerse: VerseKey?,
    version: Int
  ): Flow<TranslationPage> {
    val keys = quranDisplayData.getAyahKeysOnPage(page)

    var pageHeader: QuranPageItem.Header? = null

    var suras: List<Sura>? = null

    var lastScrollIndex = -1

    return combine(
      verseRepository.getVerses(page, version),
      getTranslation(page),
      bookmarkRepository.getBookmarksWithKeys(keys),
    ) { verses, translations, bookmarks ->
      if (suras == null) {
        suras = surahRepository.getSurahsInPage(page)
      }

      if (pageHeader == null) {
        pageHeader = pageHeaderUseCase(page)
      }

      val data = mutableListOf<TranslationPage.Row>()

      verses.forEachIndexed { index, verse ->
        val verseKey = verse.key
        if (verseKey.aya == 1) {
          val surah = suras!!.find { it.number == verseKey.sura }!!
          data.add(TranslationPage.Surah(surah.number, surah.nameSimple))
        }

        if (index > 0 && verse.key.aya != 1) data.add(
          TranslationPage.Divider("d$index")
        )


        val highLighted = verseKey.aya == selectedVerse?.aya && verseKey.sura == selectedVerse.aya

        val isBookmarked =
          bookmarks.any { verseKey.aya == it.key.aya && it.key.sura == verseKey.sura }

        data.add(
          TranslationPage.Verse(
            words = verse.words.map {
              it.toUiState(
                playing = false,
                selected = false,
              )
            },
            audioState = AudioState.PAUSED,
            isBookmarked = isBookmarked,
            highLighted = highLighted,
            verseKey = verseKey,
            id = verse.id
          )
        )

        translations.forEach { (edition, verseTranslations) ->
          val translation = verseTranslations.getOrNull(index)
          if (translation != null) {
            data.add(
              TranslationPage.Translation(
                translation = translation,
                audioState = AudioState.PAUSED,
                isBookmarked = isBookmarked,
                highLighted = highLighted,
                authorName = edition.authorName,
              )
            )
          }
        }

        data.add(
          TranslationPage.AyahToolbar(
            verseKey = verseKey,
            audioState = AudioState.PAUSED,
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
    }
  }
}