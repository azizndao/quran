@file:UnstableApi

package org.quran.core.audio

import android.content.Context
import android.os.Environment
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.DatabaseProvider
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.HttpDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.offline.DownloadManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.quran.core.audio.repositories.QariRepository
import org.quran.core.audio.api.AudioApiService
import org.quran.core.audio.datasources.RecitationsDataSource
import org.quran.core.audio.repositories.RecitationRepository
import org.quran.core.audio.repositories.TimingHelper
import org.quran.network.NetworkModule
import retrofit2.Retrofit
import retrofit2.create
import java.io.File
import java.util.concurrent.Executors

val AudioCoreModule = module {

  includes(NetworkModule)

  factory {
    Retrofit.Builder()
      .baseUrl("https://quran-data-alpha.vercel.app/")
      .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaType()))
      .build()
      .create<AudioApiService>()
  }

  factoryOf(::QariRepository)
  factoryOf(::RecitationRepository)
  factoryOf(::TimingHelper)
  factoryOf(::RecitationsDataSource)

  // factoryOf(::PlaybackConnection)

  single {
    DownloadManager(
      get(),
      get(),
      get<Cache>(),
      get<HttpDataSource.Factory>(),
      Executors.newFixedThreadPool(6)
    )
  }

  singleOf(::StandaloneDatabaseProvider) bind DatabaseProvider::class

  single {
    val context = get<Context>()
    val downloadContentDirectory = File(
      context.getExternalFilesDir(null) ?: context.filesDir,
      Environment.DIRECTORY_MUSIC
    )
    SimpleCache(downloadContentDirectory, NoOpCacheEvictor(), get<DatabaseProvider>())
  } bind Cache::class

  single { OkHttpDataSource.Factory(OkHttpClient()) } bind HttpDataSource.Factory::class
}
