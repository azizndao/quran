package org.quran.network.audio

import arg.quran.models.audio.AudioFile
import arg.quran.models.audio.AyaTiming

interface AudioApiService {

  suspend fun getAudioFiles(reciterId: Int, language: String = "en"): List<AudioFile>

  suspend fun getTimings(reciterId: Int, language: String = "en"): List<AyaTiming>
}