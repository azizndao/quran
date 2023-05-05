package org.quran.qari.repository

import android.content.Context
import android.preference.PreferenceManager
import arg.quran.models.audio.Qari
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import org.quran.qari.util.QariUtil

class CurrentQariManager (appContext: Context, private val qariUtil: QariUtil) {
  private val prefs = PreferenceManager.getDefaultSharedPreferences(appContext)
  private val currentQariFlow = MutableStateFlow(prefs.getInt(PREF_DEFAULT_QARI, 0))
  private val qaris by lazy { qariUtil.getQariItemList() }

  fun flow(): Flow<Qari> = currentQariFlow
    .map { qariId -> qaris.firstOrNull { it.id == qariId } ?: qaris.first() }

  fun setCurrentQari(qariId: Int) {
    prefs.edit().putInt(PREF_DEFAULT_QARI, qariId).apply()
    currentQariFlow.value = qariId
  }

  companion object {
    const val PREF_DEFAULT_QARI = "defaultQari"
  }
}
