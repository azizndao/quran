package org.alquran

import org.alquran.ui.MainActivity
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module
import org.muslimsapp.quran.search.FeatureSearchModule
import org.muslimsapp.quran.translations.FeatureTranslationModule
import org.quran.core.audio.AudioCoreModule
import org.quran.core.audio.PlaybackConnection
import org.quran.features.home.FeatureHomeModule
import org.quran.features.pager.FeaturePagerModule
import org.quran.features.verse_menu.FeatureVerseMenuModule

val AppModule = module {

  includes(
    AudioCoreModule,

    FeatureHomeModule,
    FeatureSearchModule,
    FeaturePagerModule,
    FeatureTranslationModule,
    FeatureVerseMenuModule,
  )

  scope<MainActivity> { scopedOf(::PlaybackConnection) }
}