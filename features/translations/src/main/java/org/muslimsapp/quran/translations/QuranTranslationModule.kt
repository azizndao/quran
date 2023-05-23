package org.muslimsapp.quran.translations

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import org.quran.datastore.di.DataStoreModule
import org.quran.translation.TranslationModule

val FeatureTranslationModule = module {
  includes(TranslationModule, DataStoreModule)

  viewModelOf(::TranslationsViewModel)
}