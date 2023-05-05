package org.quran.qari.cache.command

import okio.FileSystem
import okio.Path
import org.quram.common.core.QuranInfo
import org.quran.qari.model.PartiallyDownloadedSura
import org.quran.qari.util.AudioFileUtil

class GappedAudioInfoCommand(
  private val quranInfo: QuranInfo,
  private val fileSystem: FileSystem
) {

  fun gappedDownloads(path: Path): Pair<List<Int>, List<PartiallyDownloadedSura>> {
    val gappedSuras = fileSystem.list(path)
      .filter { it.name.toIntOrNull() in 1..114 }
      .associate { directory ->
        val gappedDownloads =
          AudioFileUtil.filesMatchingSuffixWithSuffixRemoved(fileSystem, directory, ".mp3")
            .mapNotNull { it.toIntOrNull() }
            .filter { it in 1..286 }
        directory.toFile().nameWithoutExtension.toInt() to gappedDownloads
      }

    val fullyDownloaded = gappedSuras
      .filter { (sura, ayat) -> quranInfo.getNumberOfAyahs(sura) == ayat.size }
      .map { it.key }
    val partiallyDownloaded = gappedSuras
      .filter { (sura, _) -> !fullyDownloaded.contains(sura) }
      .filter { (_, partiallyDownloadedAyat) -> partiallyDownloadedAyat.isNotEmpty() }
      .map { (sura, partiallyDownloadedAyat) ->
        PartiallyDownloadedSura(
          sura,
          quranInfo.getNumberOfAyahs(sura),
          partiallyDownloadedAyat
        )
      }
    return fullyDownloaded to partiallyDownloaded
  }
}
