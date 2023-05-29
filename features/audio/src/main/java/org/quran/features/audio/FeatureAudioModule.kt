package org.quran.features.audio

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import org.quram.common.CommonModule
import org.quran.core.audio.AudioCoreModule

val FeatureAudioModule = module {
  includes(AudioCoreModule, CommonModule)

  viewModelOf(::AudioViewModel)
}