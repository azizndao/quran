package org.quran.translation

import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.quran.translation.repositories.TranslationRepository
import org.quran.translation.repositories.TranslationRepositoryImpl
import org.quran.translation.workers.DownloadTranslationWorker

val TranslationModule = module {

    workerOf(::DownloadTranslationWorker)

    factoryOf(::TranslationRepositoryImpl) bind TranslationRepository::class
}