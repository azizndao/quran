package org.alquran.verses

import androidx.room.Room
import org.alquran.verses.databases.VersesDatabase
import org.alquran.verses.repository.VerseRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val VersesModule = module {

  factoryOf(::VerseRepository)

  single {
    Room
      .databaseBuilder(get(), VersesDatabase::class.java, "quran.v1.db")
      .build()
  }
}