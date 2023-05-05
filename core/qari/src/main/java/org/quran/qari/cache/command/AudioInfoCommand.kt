package org.quran.qari.cache.command

import arg.quran.models.audio.Qari
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import org.quran.qari.model.QariDownloadInfo
import org.quran.qari.util.QariUtil

class AudioInfoCommand(
  private val fileSystem: FileSystem,
  private val qariUtil: QariUtil,
  private val gappedAudioInfoCommand: GappedAudioInfoCommand,
  private val gaplessAudioInfoCommand: GaplessAudioInfoCommand
) {

  fun generateAllQariDownloadInfo(audioDirectory: String): List<QariDownloadInfo> {
    val path = audioDirectory.toPath()
    val directories = fileSystem.listOrNull(path) ?: emptyList()

    val folders = directories.filter { it.toFile().isDirectory }
    val qaris = qariUtil.getQariItemList()
    return qaris.map { qari ->
      val matchingPath = folders.firstOrNull { it.name == qari.slug }
      generateQariDownloadInfoWithPath(qari, matchingPath)
    }
  }

  fun generateQariDownloadInfo(qari: Qari, audioDirectory: String): QariDownloadInfo {
    val path = audioDirectory.toPath()
    val directories = fileSystem.listOrNull(path) ?: emptyList()

    val folders = directories.filter { it.toFile().isDirectory }
    val matchingPath = folders.firstOrNull { it.name == qari.slug }
    return generateQariDownloadInfoWithPath(qari, matchingPath)
  }

  private fun generateQariDownloadInfoWithPath(qari: Qari, path: Path?): QariDownloadInfo {
    return if (qari.isGapless) {
      val (fullDownloads, partialDownloads) =
        if (path == null) {
          emptyList<Int>() to emptyList()
        } else {
          gaplessAudioInfoCommand.gaplessDownloads(path)
        }
      QariDownloadInfo.GaplessQariDownloadInfo(qari, fullDownloads, partialDownloads)
    } else {
      val (fullDownloads, partiallyDownloadedSuras) =
        if (path == null) {
          emptyList<Int>() to emptyList()
        } else {
          gappedAudioInfoCommand.gappedDownloads(path)
        }
      QariDownloadInfo.GappedQariDownloadInfo(qari, fullDownloads, partiallyDownloadedSuras)
    }
  }
}
