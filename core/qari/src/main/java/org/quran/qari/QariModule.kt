package org.quran.qari

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.quran.qari.api.AudioUpdateService
import org.quran.qari.cache.AudioCacheInvalidator
import org.quran.qari.cache.QariDownloadInfoManager
import org.quran.qari.cache.QariDownloadInfoStorageCache
import org.quran.qari.cache.command.AudioInfoCommand
import org.quran.qari.cache.command.GaplessAudioInfoCommand
import org.quran.qari.cache.command.GappedAudioInfoCommand
import org.quran.qari.databases.AudioDatabaseVersionChecker
import org.quran.qari.repository.CurrentQariManager
import org.quran.qari.util.*
import retrofit2.Retrofit

val QariModule = module {
  factory {
    Retrofit.Builder()
      .baseUrl("")
      .build()
      .create(AudioUpdateService::class.java)
  }

  single { org.quran.qari.util.QariUtil() }

  factoryOf(::CurrentQariManager)

  factory { MD5Calculator } bind HashCalculator::class

  factoryOf(::AudioFileCheckerImpl) bind AudioFileChecker::class

  factoryOf(::AudioDatabaseVersionChecker) bind VersionableDatabaseChecker::class

  factoryOf(::QariDownloadInfoStorageCache)

  factoryOf(::QariDownloadInfoManager)

  factoryOf(::AudioCacheInvalidator)

  factoryOf(::GappedAudioInfoCommand)

  factoryOf(::GaplessAudioInfoCommand)

  factoryOf(::AudioInfoCommand)
}