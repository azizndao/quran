package org.alquran.hafs

import androidx.room.Room
import org.alquran.hafs.databases.VersesDatabase
import org.alquran.hafs.repository.VerseRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val HafsModule = module {

  factoryOf(::VerseRepository)

  single {
    Room
      .databaseBuilder(get(), VersesDatabase::class.java, "quran.v1.db")
      .build()
  }
}