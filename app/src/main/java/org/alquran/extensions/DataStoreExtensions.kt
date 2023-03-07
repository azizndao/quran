package org.muslimapp.feature.quran.extensions

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


operator fun <T> DataStore<Preferences>.get(key: Preferences.Key<T>): Flow<T?> {
  return data.map { prefs -> prefs[key] }.distinctUntilChanged()
}

operator fun <T> DataStore<Preferences>.get(
  key: Preferences.Key<T>,
  default: T,
): Flow<T> {
  return data.map { prefs -> prefs[key] ?: default }.distinctUntilChanged()
}

suspend fun <T> DataStore<Preferences>.set(key: Preferences.Key<T>, value: T?) {
  withContext(Dispatchers.IO) {
    edit { prefs ->
      if (value == null) {
        prefs.remove(key)
      } else {
        prefs[key] = value
      }
    }
  }
}
