package org.quran.features.pager

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.quran.features.pager.useCase.GetMushafPage
import org.quran.features.pager.useCase.GetTranslationPage
import org.quran.features.pager.useCase.PageHeaderUseCase

val FeaturePagerModule = module {

  factoryOf(::GetTranslationPage)

  factoryOf(::GetMushafPage)

  factoryOf(::PageHeaderUseCase)

  viewModelOf(::QuranPagerViewModel)
}