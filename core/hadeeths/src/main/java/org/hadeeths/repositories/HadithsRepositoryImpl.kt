package org.hadeeths.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.hadeeths.dao.HadithBookmarkDao
import org.hadeeths.dao.HadithDao
import org.hadeeths.models.*
import org.hadeeths.services.HadithApiService

internal class HadithsRepositoryImpl(
  private val hadithDao: HadithDao,
  private val bookmarkDao: HadithBookmarkDao,
  private val hadithApiService: HadithApiService,
) : HadithsRepository {

  override suspend fun insertSummaries(summaries: List<HadithSummaryEntity>) {
    hadithDao.insertSummaries(summaries)
  }

  override fun getLanguages(): Flow<List<HadithLanguage>> {
    return hadithDao.getAllLanguages().map { languages ->
      languages.ifEmpty {
        hadithApiService.getAllLanguages().also { hadithDao.insertLanguage(it) }
      }
    }
  }

  override fun getCategories(language: String): Flow<List<HadithCategory>> {
    return hadithDao.getAllCategories(language).map { categories ->
      categories.ifEmpty {
        val categoryList = hadithApiService.getCategories(language)
          .map { it.toHadithCategory(language) }
        categoryList.also { list ->
          hadithDao.insertCategories(list)
        }
      }
    }
  }

  override fun getHadithSummaries(category: HadithCategory): Flow<List<HadithSummary>> =
    hadithDao.getHadeethSummaries(category.id, category.language).map { summaries ->
      summaries.ifEmpty {
        hadithApiService.paginateHadeethSummaries(
          category.id,
          category.language,
          1,
          10000
        ).data.map { it.toEntity(category.language, categoryId = category.id) }
          .also { hadithDao.insertSummaries(it) }
        hadithDao.getHadeethSummaries(category.id, category.language).first()
      }
    }

  override suspend fun getHadith(id: Int): Hadith {
    return hadithDao.getHadeeth(id) ?: hadithApiService.getHadeeth(id)
      .also { hadithDao.insertHadeeths(it) }
  }

  override suspend fun getTranslatedHadith(id: Int, language: String): TranslatedHadith {
    return hadithDao.getTranslatedHadeeths(id, language)
      ?: hadithApiService.getTranslatedHadeeth(id, language)
        .also { hadithDao.insertTranslatedHadeeths(listOf(it)) }
  }

  override suspend fun addBookmark(hadithId: Int) {
    bookmarkDao.insert(HadithBookmark(hadithId = hadithId))
  }

  override suspend fun removeBookmark(hadithId: Int) {
    bookmarkDao.remove(hadithId)
  }

  override fun getBookmarkByHadithId(hadithId: Int): Flow<HadithBookmark?> {
    return bookmarkDao.getByHadithId(hadithId)
  }

  override suspend fun getBookmarkByHadithIds(hadithIds: Set<Int>): List<HadithBookmark> {
    return bookmarkDao.getByHadithIds(hadithIds)
  }

  override fun getBookmarkedHadiths() = hadithDao.getBookmarkedHadiths()
  override suspend fun searchHadith(query: String, language: String): List<HadithResult> {
    return hadithDao.search(query, language)
  }

  override suspend fun searchHadith(
    query: String,
    language: String,
    categoryId: Int,
  ): List<HadithResult> {
    return hadithDao.search(query, language, categoryId)
  }
}