package org.quran.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import org.quran.datastore.AudioPreferences
import org.quran.datastore.QuranPreferences
import org.quran.datastore.ReadingPositionPreferences
import org.quran.datastore.TranslationList
import org.quran.datastore.repositories.AudioPreferencesRepository
import org.quran.datastore.repositories.QuranPreferencesRepository
import org.quran.datastore.repositories.ReadingPreferencesRepository
import org.quran.datastore.serializers.AudioPreferencesSerializer
import org.quran.datastore.serializers.QuranPreferencesSerializer
import org.quran.datastore.serializers.ReadingPositionSerializer
import org.quran.datastore.serializers.TranslationsListSerializer




val DataStoreModule = module {
  single {
    AudioPreferencesRepository(
      DataStoreFactory.create(
        corruptionHandler = ReplaceFileCorruptionHandler { AudioPreferences.getDefaultInstance() },
        produceFile = { get<Context>().dataStoreFile("audio") },
        serializer = AudioPreferencesSerializer()
      )
    )
  }

  single {
    QuranPreferencesRepository(
      DataStoreFactory.create(
        corruptionHandler = ReplaceFileCorruptionHandler { QuranPreferences.getDefaultInstance() },
        produceFile = { get<Context>().dataStoreFile("quran") },
        serializer = QuranPreferencesSerializer()
      ),
    )
  }

  single<DataStore<TranslationList>>(TranslationDataStoreQualifier) {
    DataStoreFactory.create(
      corruptionHandler = ReplaceFileCorruptionHandler { TranslationList.getDefaultInstance() },
      produceFile = { get<Context>().dataStoreFile("translations") },
      serializer = TranslationsListSerializer()
    )
  }

  single {
    ReadingPreferencesRepository(
      DataStoreFactory.create(
        corruptionHandler = ReplaceFileCorruptionHandler { ReadingPositionPreferences.getDefaultInstance() },
        produceFile = { get<Context>().dataStoreFile("reading") },
        serializer = ReadingPositionSerializer()
      )
    )
  }

  single {
    PreferenceDataStoreFactory.create(
      corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
      produceFile = { get<Context>().dataStoreFile("settings.preferences_pb") },
    )
  }
}