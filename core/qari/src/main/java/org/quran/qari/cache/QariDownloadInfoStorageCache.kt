package org.quran.qari.cache

import org.quran.qari.model.QariDownloadInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal class QariDownloadInfoStorageCache {
  private val cache = MutableStateFlow<List<QariDownloadInfo>>(emptyList())

  fun flow(): Flow<List<QariDownloadInfo>> = cache

  fun lastValue(): List<QariDownloadInfo> = cache.value

  fun writeAll(qariDownloads: List<QariDownloadInfo>) {
    cache.value = qariDownloads
  }
}
