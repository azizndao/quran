package org.quran.domain.quran.workers

import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module

internal val WorkerModule = module {
  workerOf(::DownloadFontsWorker)
  workerOf(::SetupDatabaseWorker)
}