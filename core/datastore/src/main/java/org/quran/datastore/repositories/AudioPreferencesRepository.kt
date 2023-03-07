package org.quran.datastore.repositories

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.map
import org.quran.datastore.AudioPreferences
import org.quran.datastore.recentPlayback

class AudioPreferencesRepository internal constructor(
  private val dataStore: DataStore<AudioPreferences>,
) {

  fun getCurrentReciter() = dataStore.data.map { it.currentReciter }

  suspend fun setCurrentReciter(reciterId: String) {
    dataStore.updateData { it.toBuilder().setCurrentReciter(reciterId).build() }
  }

  fun getPlaybackHistory() = dataStore.data.map { it.recentPlayback }

  suspend fun setRecentPlayback(reciterId: String, sura: Int, aya: Int) {
    dataStore.updateData {
      val playback = recentPlayback {
        this.reciterId = reciterId
        surah = sura
        ayah = aya
      }
      it.toBuilder().setRecentPlayback(playback).build()
    }
  }
}
