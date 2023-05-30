package org.quran.features.reciter

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import org.quram.common.CommonModule
import org.quran.core.audio.AudioCoreModule

val QariFeatureModule = module {
  includes(CommonModule, AudioCoreModule)

  viewModelOf(::QariViewModel)
}