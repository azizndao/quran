package org.quran.qari.databases

import org.quran.qari.VersionableDatabaseChecker

class AudioDatabaseVersionChecker : VersionableDatabaseChecker {
  override fun getVersionForDatabase(path: String): Int {
    return SuraTimingDatabaseHandler.getDatabaseHandler(path).getVersion()
  }
}
