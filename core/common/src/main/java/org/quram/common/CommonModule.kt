package org.quram.common

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.quram.common.core.QuranInfo
import org.quram.common.core.QuranPageInfo
import org.quram.common.core.QuranPageInfoImpl
import org.quram.common.pageinfo.common.MadaniDataSource
import org.quram.common.repositories.SurahRepository
import org.quram.common.source.MadaniPageProvider
import org.quram.common.source.PageProvider
import org.quram.common.source.QuranDataSource
import org.quram.common.utils.*

val CommonModule = module {

  factoryOf(::MadaniPageProvider) bind PageProvider::class

  factoryOf(::MadaniDataSource) bind QuranDataSource::class

  factoryOf(::QuranPageInfoImpl) bind QuranPageInfo::class

  factoryOf(::QuranDisplayData)

  factoryOf(::Rub3DisplayUseCase)

  singleOf(::QuranInfo)

  factoryOf(::SurahRepository)
}