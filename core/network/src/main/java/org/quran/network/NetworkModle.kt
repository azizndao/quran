package org.quran.network

import kotlinx.serialization.json.Json
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module
import org.quran.network.workers.DownloadFileWorker
import org.quran.network.workers.ExtractArchiveWorker

val NetworkModule = module {

  workerOf(::DownloadFileWorker)
  workerOf(::ExtractArchiveWorker)

  single {
    Json {
      ignoreUnknownKeys = true
      prettyPrint = true
      isLenient = true
    }
  }
}
