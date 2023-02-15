package org.alquran.usecases

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val UseCaseModule = module {
    factoryOf(::ListSurahUseCase)
    factoryOf(::GetMushafPage)
    factoryOf(::PageHeaderUseCase)
    factoryOf(::GetTranslationPage)
    factoryOf(::GetListOfHizbUseCase)
}