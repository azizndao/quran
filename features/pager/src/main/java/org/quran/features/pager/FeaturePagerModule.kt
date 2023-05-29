package org.quran.features.pager

import org.alquran.verses.VersesModule
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.quran.bookmarks.BookmarkModule
import org.quran.core.audio.AudioCoreModule
import org.quran.features.pager.useCase.GetQuranPageUseCase
import org.quran.features.pager.useCase.GetTranslationPageUseCase
import org.quran.features.pager.useCase.PageHeaderUseCase
import org.quran.translation.TranslationModule

val FeaturePagerModule = module {

  includes(AudioCoreModule, VersesModule, TranslationModule, BookmarkModule)

  factoryOf(::GetTranslationPageUseCase)

  factoryOf(::GetQuranPageUseCase)

  factoryOf(::PageHeaderUseCase)

  viewModelOf(::QuranPagerViewModel)
}