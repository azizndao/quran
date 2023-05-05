package org.quran.qari.model

import arg.quran.models.audio.Qari

sealed class QariDownloadInfo {
  abstract val qari: Qari
  abstract val fullyDownloadedSuras: List<Int>

  data class GaplessQariDownloadInfo(
    override val qari: Qari,
    override val fullyDownloadedSuras: List<Int>,
    val partiallyDownloadedSuras: List<Int>
  ) : QariDownloadInfo()

  data class GappedQariDownloadInfo(
    override val qari: Qari,
    override val fullyDownloadedSuras: List<Int>,
    val partiallyDownloadedSuras: List<PartiallyDownloadedSura>
  ) : QariDownloadInfo()
}
