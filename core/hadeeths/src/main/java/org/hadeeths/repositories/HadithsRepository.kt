package org.hadeeths.repositories

import kotlinx.coroutines.flow.Flow
import org.hadeeths.models.*

interface HadithsRepository {

  suspend fun insertSummaries(summaries: List<HadithSummaryEntity>)

  fun getLanguages(): Flow<List<HadithLanguage>>

  fun getCategories(language: String): Flow<List<HadithCategory>>

  fun getHadithSummaries(category: HadithCategory): Flow<List<HadithSummary>>

  suspend fun getHadith(id: Int): Hadith

  suspend fun getTranslatedHadith(id: Int, language: String): TranslatedHadith

  suspend fun addBookmark(hadithId: Int)

  suspend fun removeBookmark(hadithId: Int)

  fun getBookmarkByHadithId(hadithId: Int): Flow<HadithBookmark?>

  suspend fun getBookmarkByHadithIds(hadithIds: Set<Int>): List<HadithBookmark>

  fun getBookmarkedHadiths(): Flow<List<HadithSummary>>

  suspend fun searchHadith(query: String, language: String): List<HadithResult>

  suspend fun searchHadith(query: String, language: String, categoryId: Int): List<HadithResult>
}