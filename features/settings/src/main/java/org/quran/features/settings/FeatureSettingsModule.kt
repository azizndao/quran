package org.quran.features.settings

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val FeatureSettingsModule = module {
  viewModelOf(::SettingsViewModel)
}