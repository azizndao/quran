package org.quran.features.pager.useCase


import arg.quran.models.Sura
import arg.quran.models.VerseRange
import arg.quran.models.quran.Verse
import arg.quran.models.quran.VerseKey
import kotlinx.collections.immutable.toPersistentList
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
import org.alquran.verses.repository.VerseRepository
import org.quram.common.core.QuranInfo
import org.quram.common.repositories.SurahRepository
import org.quram.common.utils.QuranDisplayData
import org.quran.bookmarks.repository.BookmarkRepository
import org.quran.core.audio.PlaybackConnection
import org.quran.datastore.LocaleTranslation
import org.quran.features.pager.uiState.QuranPageItem
import org.quran.features.pager.uiState.TranslationPage
import org.quran.translation.repositories.TranslationsRepository
import org.quran.translation.repositories.VerseTranslationRepository
import timber.log.Timber

data class LocalTranslationContent(
  val translation: LocaleTranslation,
  val verses: List<Verse>,
)

class GetTranslationPage(
  private val verseRepository: VerseRepository,
  private val bookmarkRepository: BookmarkRepository,
  private val surahRepository: SurahRepository,
  private val pageHeaderUseCase: PageHeaderUseCase,
  private val quranDisplayData: QuranDisplayData,
  private val translationRepository: TranslationsRepository,
  private val verseTranslationRepository: VerseTranslationRepository,
  private val playbackConnection: PlaybackConnection,
  private val quranInfo: QuranInfo,
) {

  lateinit var coroutineScope: CoroutineScope

  private val _caches by lazy {
    translationRepository.observeSelectedEditions().flatMapMerge { translations ->
      if (translations.isEmpty()) return@flatMapMerge flowOf(emptyList())
      val flowList = translations.map { locale ->
        verseTranslationRepository.observeAllVerse(locale.slug)
          .map { verses -> LocalTranslationContent(locale, verses) }
      }

      combine(flowList) { editions -> editions.filter { it.verses.isNotEmpty() } }
    }.catch { Timber.e(it) }
      .stateIn(coroutineScope, SharingStarted.WhileSubscribed(5_000), emptyList())
  }

  private fun getSelectedTranslationEditions(
    range: VerseRange,
  ): Flow<List<LocalTranslationContent>> {
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
        LocalTranslationContent(locale, pageVerses)
      }
    }
  }

  operator fun invoke(page: Int, selectedVerse: VerseKey?, version: Int): Flow<TranslationPage> {
    val keys = quranDisplayData.getAyahKeysOnPage(page)

    var pageHeader: QuranPageItem.Header? = null

    var suras: List<Sura>? = null

    var lastScrollIndex = -1

    val range = quranInfo.getVerseRangeForPage(page)

    return combine(
      verseRepository.getVerses(range),
      getSelectedTranslationEditions(range).filter(List<LocalTranslationContent>::isNotEmpty),
      bookmarkRepository.observeBookmarksWithKeys(keys),
      playbackConnection.currentAyah,
    ) { verses, translations, bookmarks, playingVerse ->

      if (suras == null) {
        suras = surahRepository.getSurahsInPage(page)
      }

      if (pageHeader == null) {
        pageHeader = pageHeaderUseCase(page)
      }

      val data = mutableListOf<TranslationPage.Row>()

      var scrollIndex = -1

      verses.forEachIndexed { index, verse ->
        val key = VerseKey(verse.sura, verse.ayah)

        if (key.aya == 1) {
          val surah = suras!!.find { it.number == key.sura }!!
          data.add(TranslationPage.Chapter(surah.number, surah.nameSimple))
        }

        if (index > 0 && verse.ayah != 1) data.add(TranslationPage.Divider("d$index"))

        val isPlaying = key == playingVerse

        val highLighted = key.aya == selectedVerse?.aya && key.sura == selectedVerse.aya

        val isBookmarked = bookmarks.any {
          key.aya == it.key.aya && it.key.sura == key.sura
        }

        data.add(
          TranslationPage.Verse(
            isPlaying = isPlaying,
            isBookmarked = isBookmarked,
            highLighted = highLighted,
            suraAyah = key,
            text = verse.text,
          )
        )

        if (isPlaying || highLighted && scrollIndex == -1) {
          scrollIndex = data.size
        }

        for ((edition, verseTranslations) in translations) {
          verseTranslations.getOrNull(index)?.let {
            data.add(
              TranslationPage.TranslatedVerse(
                suraAyah = key,
                authorName = edition.authorName,
                text = it.text,
                isPlaying = isPlaying,
                isBookmarked = isBookmarked,
                highLighted = highLighted,
              )
            )
          }
        }

        data.add(
          TranslationPage.VerseToolbar(
            suraAyah = key,
            isPlaying = isPlaying,
            isBookmarked = isBookmarked,
            highLighted = highLighted,
          )
        )
      }

      TranslationPage(
        header = pageHeader!!,
        page = page,
        items = data.toPersistentList(),
        firstItem = lastScrollIndex == -1,
        scrollIndex = scrollIndex.also { lastScrollIndex = it },
      )
    }.flowOn(Dispatchers.IO)
  }
}