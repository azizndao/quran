package org.quran.qari.cache

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.quram.common.core.QuranFileManager
import org.quran.qari.cache.command.AudioInfoCommand
import org.quran.qari.model.QariDownloadInfo

class QariDownloadInfoManager(
  private val quranFileManager: QuranFileManager,
  private val audioInfoCommand: AudioInfoCommand,
) {
  private val scope: CoroutineScope = CoroutineScope(SupervisorJob())
  private val storageCache = QariDownloadInfoStorageCache()

  fun downloadedQariInfo(): Flow<List<QariDownloadInfo>> {
    return storageCache.flow()
  }

  fun downloadQariInfoFilteringNonDownloadedGappedQaris(): Flow<List<QariDownloadInfo>> {
    return downloadedQariInfo().map { list ->
      list.filter { qariDownloadInfo ->
        val qari = qariDownloadInfo.qari
        val gappedItem = qariDownloadInfo as? QariDownloadInfo.GappedQariDownloadInfo
        qari.isGapless ||
          // gapped qaris are only shown if they don't have a gapless alternative or if
          // some file for the qari is already downloaded.
          ( qariDownloadInfo.fullyDownloadedSuras.isNotEmpty() ||
            (gappedItem?.partiallyDownloadedSuras?.isNotEmpty() ?: false))
      }
    }
  }

  private fun populateCache() {
    val audioDirectory = quranFileManager.audioFileDirectory() ?: return
    val qariDownloadInfo = audioInfoCommand.generateAllQariDownloadInfo(audioDirectory)
    storageCache.writeAll(qariDownloadInfo)
  }

  private suspend fun updateQariInformationForQariId(qariId: Int) {
    val qariDownloadInfo = getUpdatedQariInformation(qariId)
    if (qariDownloadInfo != null) {
      withContext(Dispatchers.Main) {
        val updated = storageCache.lastValue()
          .map {
            // latest last value, but replace the qari item with our updated one
            if (it.qari.id == qariDownloadInfo.qari.id) qariDownloadInfo else it
          }
        storageCache.writeAll(updated)
      }
    }
  }

  private fun getUpdatedQariInformation(qariId: Int): QariDownloadInfo? {
    val lastInfo = storageCache.lastValue()
    val updatedQari = lastInfo.firstOrNull { it.qari.id == qariId } ?: return null
    val audioDirectory = quranFileManager.audioFileDirectory()
    return if (audioDirectory != null) {
      audioInfoCommand.generateQariDownloadInfo(updatedQari.qari, audioDirectory)
    } else {
      null
    }
  }
}
