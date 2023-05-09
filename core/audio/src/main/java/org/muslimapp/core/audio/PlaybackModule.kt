@file:UnstableApi

package org.muslimapp.core.audio

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
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.muslimapp.core.audio.datasources.RecitationsDataSource
import org.muslimapp.core.audio.repositories.QariRepository
import org.muslimapp.core.audio.repositories.RecitationRepository
import org.muslimapp.core.audio.repositories.TimingRepository
import java.io.File
import java.util.concurrent.Executors

val PlaybackModule = module {

  singleOf(::PlaybackConnection)

  factoryOf(::QariRepository)

  factoryOf(::RecitationRepository)

  factoryOf(::TimingRepository)

  factoryOf(::RecitationsDataSource)

  single {
    DownloadManager(
      get(),
      get(),
      get(),
      get<HttpDataSource.Factory>(),
      Executors.newFixedThreadPool(6)
    )
  }

  singleOf(::StandaloneDatabaseProvider) bind DatabaseProvider::class

  single {
    val downloadContentDirectory = File(
      get<Context>().getExternalFilesDir(null) ?: get<Context>().filesDir,
      Environment.DIRECTORY_MUSIC
    )
    SimpleCache(downloadContentDirectory, NoOpCacheEvictor(), get<DatabaseProvider>())
  } bind Cache::class

  single { OkHttpDataSource.Factory(OkHttpClient()) } bind HttpDataSource.Factory::class
}