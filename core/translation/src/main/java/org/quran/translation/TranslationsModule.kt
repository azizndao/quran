package org.quran.translation

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import org.quran.datastore.TranslationList
import org.quran.datastore.di.DataStoreModule
import org.quran.datastore.di.TranslationDataStoreQualifier
import org.quran.translation.api.TranslationApiService
import org.quran.translation.repositories.TranslationsRepository
import org.quran.translation.repositories.TranslationsRepositoryImpl
import org.quran.translation.repositories.VerseTranslationRepository
import org.quran.translation.repositories.VerseTranslationRepositoryImpl
import org.quran.translation.workers.DownloadTranslationWorker
import retrofit2.Retrofit

val TranslationModule = module {
  includes(DataStoreModule)

  factoryOf(::VerseTranslationRepositoryImpl) bind VerseTranslationRepository::class

  factory {
    TranslationsRepositoryImpl(
      get(),
      get(),
      get(TranslationDataStoreQualifier)
    )
  } bind TranslationsRepository::class

  factory {
    Retrofit.Builder()
      .baseUrl("https://api.quran.com/api/v4/")
      .addConverterFactory(get<Json>().asConverterFactory(MediaType.get("application/json")))
      .build()
      .create(TranslationApiService::class.java)
  }

  workerOf(::DownloadTranslationWorker)
}
