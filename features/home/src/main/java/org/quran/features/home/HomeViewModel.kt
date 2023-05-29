package org.quran.features.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import arg.quran.models.HizbQuarter
import arg.quran.models.QuarterMapping
import arg.quran.models.quran.VerseKey
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.alquran.verses.repository.VerseRepository
import org.quram.common.core.QuranInfo
import org.quram.common.repositories.SurahRepository
import org.quran.bookmarks.repository.BookmarkRepository
import org.quran.features.home.juzs.JuzListUiState
import org.quran.features.home.surahs.SurahListUiState

internal class HomeViewModel(
  private val quranInfo: QuranInfo,
  private val verseRepository: VerseRepository,
  private val surahRepository: SurahRepository,
  bookmarkRepository: BookmarkRepository,
  app: Application,
) : AndroidViewModel(app) {

  val surahsFlow: StateFlow<SurahListUiState> = flow {
    val surahs = surahRepository.getJuzsSurahs()
    if (surahs.isEmpty()) return@flow emit(SurahListUiState())

    emit(SurahListUiState(loading = false, recentSurah = null, juzs = surahs.toPersistentList()))
  }.flowOn(Dispatchers.IO).stateIn(viewModelScope, SharingStarted.Eagerly, SurahListUiState())

  val hibzUiStateFlow = MutableStateFlow(JuzListUiState(data = getListOfHizb(), loading = false))


  val bookmarkUiStateFlow = bookmarkRepository.observeTabWithBookmarks().map { tags ->
    tags.toPersistentList()
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), persistentListOf())

  init {
    viewModelScope.launch {
      if (verseRepository.hasMissingVerses()) {
        verseRepository.setupDatabase()
      }
    }
  }

  private fun getListOfHizb() = buildList {
    val quarters: Array<VerseKey> = quranInfo.quarters
    val surahs = surahRepository.getAllSurahs()
    var index = 0
    for (j in 1..30) {
      val rub3s = List(8) {
        val (sura, ayah) = quarters[index++]
        HizbQuarter(
          surahName = surahs[sura - 1].nameSimple,
          surahNumber = sura,
          ayahNumberInSurah = ayah,
          juz = j,
          page = quranInfo.getPageFromSuraAyah(sura, ayah),
          hizbQuarter = index + 1
        )
      }

      add(
        QuarterMapping(
          number = j,
          page = rub3s.first().page,
          quarters = rub3s,
        )
      )
    }
  }.toPersistentList()

  fun getPage(verse: VerseKey): Int {
    return quranInfo.getPageFromSuraAyah(verse.sura, verse.sura)
  }
}