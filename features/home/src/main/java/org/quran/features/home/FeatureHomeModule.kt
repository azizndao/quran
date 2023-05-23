package org.quran.features.home

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import org.quram.common.CommonModule

val FeatureHomeModule = module {
  includes(CommonModule)

  viewModelOf(::HomeViewModel)
}