package org.quran.qari

interface VersionableDatabaseChecker {
  fun getVersionForDatabase(path: String): Int
}
