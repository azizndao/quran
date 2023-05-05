package org.quran.qari.util

import org.quran.qari.model.QariItem

interface AudioFileChecker {
  fun isQariOnFilesystem(qari: QariItem): Boolean
  fun doesFileExistForQari(qari: QariItem, file: String): Boolean
  fun doesHashMatchForQariFile(qari: QariItem, file: String, hash: String): Boolean
  fun getDatabasePathForQari(qari: QariItem): String?
  fun doesDatabaseExist(path: String): Boolean
}
