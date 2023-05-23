package org.alquran.verses.repository

import android.content.Context
import arg.quran.models.VerseRange
import arg.quran.models.quran.QuranWord
import arg.quran.models.quran.Verse
import arg.quran.models.quran.VerseKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.alquran.verses.local.VersesDatabase
import org.alquran.verses.local.models.QuranicVerse
import org.alquran.verses.local.models.SearchableVerse
import org.quram.common.core.QuranInfo
import timber.log.Timber
import java.util.zip.ZipInputStream


class VerseRepository internal constructor(
  private val context: Context,
  private val database: VersesDatabase,
  private val quranInfo: QuranInfo,
) {

  private val _verseCache = database.verses.getVersesFlow()

  suspend fun setupDatabase() {
    withContext(Dispatchers.IO) {
      context.assets.open("quran.v1.zip").use { input ->
        ZipInputStream(input).use { zipInput ->
          var entry = zipInput.nextEntry
          while (entry != null) {
            val content = zipInput.reader().readText()
            Timber.tag("VerseRepository").d("Extracting file ${entry.name}")
            when (entry.name) {
              "verses" -> insertVerses(Json.decodeFromString(content))
              "searchables" -> invertSearchableVerses(Json.decodeFromString(content))
              "words" -> invertVerseWords(Json.decodeFromString(content))
            }
            entry = zipInput.nextEntry
          }
        }
      }
    }
  }

  private suspend fun insertVerses(verses: List<QuranicVerse>) {
    database.verses.insert(verses.map { QuranicVerse(it.sura, it.ayah, it.text) })
  }

  private suspend fun invertSearchableVerses(verses: List<SearchableVerse>) {
    database.searchables.insert(verses.map { SearchableVerse(0, it.sura, it.ayah, it.text) })
  }

  private suspend fun invertVerseWords(words: List<QuranWord>) {
    database.words.insert(words)
  }

  suspend fun hasMissingVerses() = database.verses.hasMissingVerses()

  fun getVerses(range: VerseRange): Flow<List<Verse>> {
    return _verseCache.map { verses ->
      if (range.startSura == range.endingSura) {
        verses
          .filter { verse ->
            verse.sura == range.endingSura &&
              verse.ayah >= range.startAyah &&
              verse.ayah <= range.endingAyah
          }
      } else
        verses.filter { v ->
          v.sura == range.startSura && v.ayah >= range.startAyah ||
            v.sura == range.endingSura && v.ayah <= range.endingAyah ||
            v.sura > range.startSura && v.sura < range.endingSura
        }.take(range.versesInRange)
    }
  }

  fun getVersesWordsByPage(page: Int): Flow<List<QuranWord>> = database.words.getAllWords(page)

  suspend fun search(query: String): List<Verse> {
    return database.searchables.search(query)
  }

  suspend fun get(suraAyah: VerseKey): Verse? {
    return _verseCache.first().find { it.sura == suraAyah.sura && it.ayah == suraAyah.aya }
  }
}