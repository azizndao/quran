package org.quran.datastore.di

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import org.koin.dsl.module
import org.quran.datastore.*
import org.quran.datastore.repositories.*
import org.quran.datastore.serializers.*


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
            DataStoreFactory.create(
                corruptionHandler = ReplaceFileCorruptionHandler { TranslationList.getDefaultInstance() },
                produceFile = { get<Context>().dataStoreFile("translations") },
                serializer = TranslationsListSerializer()
            ),
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
            produceFile = { get<Context>().dataStoreFile("user_settings") },
        )
    }
}