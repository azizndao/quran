package org.quran.features.saved

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val FeatureSavedModule = module {
  viewModelOf(::SavedViewModel)
}