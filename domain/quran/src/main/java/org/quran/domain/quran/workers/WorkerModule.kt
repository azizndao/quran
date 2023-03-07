package org.quran.domain.quran.workers

import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module

val WorkerModule = module {
  workerOf(::DownloadVersesWorker)
  workerOf(::DownloadFontsWorker)
  workerOf(::SyncWorker)
}