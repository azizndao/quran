package org.alquran

import org.alquran.ui.MainActivity
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module
import org.muslimsapp.quran.search.FeatureSearchModule
import org.muslimsapp.quran.translations.FeatureTranslationModule
import org.quran.core.audio.AudioCoreModule
import org.quran.core.audio.PlaybackConnection
import org.quran.features.audio.FeatureAudioModule
import org.quran.features.home.FeatureHomeModule
import org.quran.features.pager.FeaturePagerModule
import org.quran.features.settings.FeatureSettingsModule
import org.quran.features.share.FeatureShareModule

val AppModule = module {

  includes(
    AudioCoreModule,

    FeatureHomeModule,
    FeatureAudioModule,
    FeatureSearchModule,
    FeatureShareModule,
    FeaturePagerModule,
    FeatureTranslationModule,
    FeatureSettingsModule
  )

  scope<MainActivity> { scopedOf(::PlaybackConnection) }
}