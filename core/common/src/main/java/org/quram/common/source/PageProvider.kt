package org.quram.common.source

interface PageProvider {
  fun getDataSource(): QuranDataSource
  fun getPageSizeCalculator(displaySize: DisplaySize): PageSizeCalculator

  fun getImageVersion(): Int

  fun getImagesBaseUrl(): String
  fun getImagesZipBaseUrl(): String
  fun getPatchBaseUrl(): String
  fun getAyahInfoBaseUrl(): String
  fun getDatabasesBaseUrl(): String
  fun getAudioDatabasesBaseUrl(): String

  fun getAudioDirectoryName(): String
  fun getDatabaseDirectoryName(): String
  fun getAyahInfoDirectoryName(): String
  fun getImagesDirectoryName(): String

  fun ayahInfoDbHasGlyphData(): Boolean = false

  fun getPageContentType(): PageContentType = PageContentType.IMAGE
  fun getFallbackPageType(): String? = null
}
