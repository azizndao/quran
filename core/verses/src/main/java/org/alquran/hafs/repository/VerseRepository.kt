package org.alquran.hafs.repository

import arg.quran.models.quran.Verse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import org.alquran.hafs.databases.VersesDatabase
import org.quran.network.verse.VerseApiService
import timber.log.Timber


class VerseRepository internal constructor(
  private val apiService: VerseApiService,
  private val database: VersesDatabase,
) {

  private val dao get() = database.dao

  suspend fun hasMissingVerses() = dao.hasMissingVerses()

  suspend fun insertVerses(items: List<Verse>) =
    dao.insertVerses(items)

  fun getVerses(page: Int, version: Int = 1): Flow<List<Verse>> =
    dao.getVersesFlow(page).onEach { verses ->
      verses.ifEmpty { downloadVerseByPage(page, version) }
    }

  private suspend fun downloadVerseByPage(page: Int, version: Int = 1) {
    Timber.tag("VerseRepository").d("Downloading v1 page content $page")

    val verses = apiService.downloadVerseByPage(page, version)

    dao.insertVerses(verses)
  }
}