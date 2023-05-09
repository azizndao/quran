package org.alquran.usecases


import android.content.Context
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import arg.quran.models.Sura
import arg.quran.models.quran.VerseKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.alquran.ui.uistate.QuranPageItem
import org.alquran.ui.uistate.TranslationPage
import org.alquran.verses.repository.VerseRepository
import org.muslimapp.core.audio.PlaybackConnection
import org.quram.common.core.QuranInfo
import org.quram.common.repositories.SurahRepository
import org.quram.common.utils.QuranDisplayData
import org.quram.common.utils.UriProvider
import org.quran.bookmarks.repository.BookmarkRepository
import org.quran.translation.local.models.TranslatedEdition
import org.quran.translation.repositories.TranslationRepository
import timber.log.Timber

class GetTranslationPage(
  private val verseRepository: VerseRepository,
  private val bookmarkRepository: BookmarkRepository,
  private val surahRepository: SurahRepository,
  private val pageHeaderUseCase: PageHeaderUseCase,
  private val quranDisplayData: QuranDisplayData,
  private val context: Context,
  private val translationRepository: TranslationRepository,
  private val playbackConnection: PlaybackConnection,
  private val quranInfo: QuranInfo,
) {

  lateinit var coroutineScope: CoroutineScope

  private val _caches by lazy {
    translationRepository.getSelectedTranslations().flatMapMerge { translations ->
      if (translations.isEmpty()) return@flatMapMerge flowOf(emptyList())
      val flowList = translations.map { locale ->
        translationRepository.getAllVerses(locale)
          .map { verses -> TranslatedEdition(locale, verses) }
      }

      combine(flowList) { editions -> editions.filter { it.verses.isNotEmpty() } }
    }.catch { Timber.e(it) }
      .stateIn(coroutineScope, SharingStarted.WhileSubscribed(5_000), emptyList())
  }

  private fun getSelectedTranslationEditions(
    page: Int,
  ): Flow<List<TranslatedEdition>> {
    val range = quranInfo.getVerseRangeForPage(page)
    return _caches.map { caches ->
      caches.map { (locale, verses) ->
        val pageVerses = if (range.startSura == range.endingSura) {
          verses.filter { verse ->
            verse.sura == range.endingSura &&
              verse.ayah >= range.startAyah &&
              verse.ayah <= range.endingAyah
          }
        } else
          verses.filter { verse ->
            verse.sura == range.startSura && verse.ayah >= range.startAyah ||
              verse.sura == range.endingSura && verse.ayah <= range.endingAyah ||
              verse.sura > range.startSura && verse.sura < range.endingSura
          }.take(range.versesInRange)
        TranslatedEdition(locale, pageVerses)
      }
    }
  }

  operator fun invoke(page: Int, selectedVerse: VerseKey?, version: Int): Flow<TranslationPage> {
    val keys = quranDisplayData.getAyahKeysOnPage(page)

    var pageHeader: QuranPageItem.Header? = null

    var suras: List<Sura>? = null

    var lastScrollIndex = -1

    return combine(
      verseRepository.getVerses(page),
      getSelectedTranslationEditions(page).filter(List<TranslatedEdition>::isNotEmpty),
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

        val translatedVerses = translations.mapNotNull { (edition, verseTranslations) ->
          verseTranslations.getOrNull(index)?.let {
            TranslationPage.TranslatedVerse(
              authorName = edition.authorName,
              text = it.text,
            )
          }
        }

        data.add(
          TranslationPage.Verse(
            isPlaying = isPlaying,
            isBookmarked = isBookmarked,
            highLighted = highLighted,
            suraAyah = VerseKey(verse.sura, verse.ayah),
            text = verse.text,
            translations = translatedVerses
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