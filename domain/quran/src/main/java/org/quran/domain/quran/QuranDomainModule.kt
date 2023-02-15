package org.quran.domain.quran

import org.koin.dsl.module
import org.quran.domain.quran.workers.WorkerModule

val QuranDomainModule = module {
    includes(WorkerModule)
}