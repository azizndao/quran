package org.alquran.verses

import androidx.room.Room
import org.alquran.verses.local.VersesDatabase
import org.alquran.verses.repository.VerseRepository
import org.alquran.verses.workers.SetupDatabaseWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val VersesModule = module {
  workerOf(::SetupDatabaseWorker)

  factoryOf(::VerseRepository)

  single {
    Room
      .databaseBuilder(get(), VersesDatabase::class.java, "quran.v1.db")
      .build()
  }
}