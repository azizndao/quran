package org.quran.datastore.repositories

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.map
import org.quran.datastore.ReadingPositionPreferences

class ReadingPreferencesRepository(
  private val dataStore: DataStore<ReadingPositionPreferences>,
) {

//    fun getSura() = dataStore.data.map { it.sura }

  fun getAyah() = dataStore.data.map { it.ayah }

  fun getPage() = dataStore.data.map { it.page }

//    fun getPosition() = dataStore.data

//    suspend fun setPosition(page: Int, sura: Int, ayah: Int) = dataStore.updateData {
//        it.toBuilder().setPage(page).setSura(sura).setAyah(ayah).build()
//    }
}