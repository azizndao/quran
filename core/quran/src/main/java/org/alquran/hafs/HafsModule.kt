package org.alquran.hafs

import org.alquran.hafs.repository.VerseRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val HafsModule = module {

    factoryOf(::VerseRepository)
}