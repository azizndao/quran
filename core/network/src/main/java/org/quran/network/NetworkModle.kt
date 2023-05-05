package org.quran.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.quran.network.audio.AudioApiService
import org.quran.network.audio.AudioApiServiceImpl
import org.quran.network.translation.TranslationApiService
import org.quran.network.translation.TranslationApiServiceImpl
import org.quran.network.verse.VerseApiService
import org.quran.network.verse.VerseApiServiceImpl

val NetworkModule = module {

  single {
    Json {
      ignoreUnknownKeys = true
      prettyPrint = true
      isLenient = true
    }
  }

  factory {
    HttpClient(OkHttp) {
      engine { config { followRedirects(true) } }
      install(ContentNegotiation) { json(get()) }
    }
  }

  factoryOf(::VerseApiServiceImpl) bind VerseApiService::class

  factoryOf(::TranslationApiServiceImpl) bind TranslationApiService::class

  factoryOf(::AudioApiServiceImpl) bind AudioApiService::class
}
