package org.muslimsapp.quran.search

import org.alquran.verses.VersesModule
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import org.quram.common.CommonModule
import org.quran.datastore.di.DataStoreModule
import org.quran.translation.TranslationModule

val FeatureSearchModule = module {
  includes(CommonModule, VersesModule, TranslationModule, DataStoreModule)

  viewModelOf(::SearchViewModel)
}