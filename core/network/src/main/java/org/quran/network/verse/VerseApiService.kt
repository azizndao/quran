package org.quran.network.verse

import arg.quran.models.quran.Verse
import java.io.File

interface VerseApiService {

  suspend fun downloadFont(page: Int, version: Int, file: File)

  suspend fun downloadVerseByPage(
    page: Int,
    version: Int,
    language: String = "en"
  ): List<Verse>

  suspend fun downloadVerseByChapter(
    chapter: Int,
    version: Int,
    language: String = "en"
  ): List<Verse>

  suspend fun downloadVerseByJuz(
    juz: Int,
    version: Int,
    language: String = "en"
  ): List<Verse>
}