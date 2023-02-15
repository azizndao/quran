package org.quram.common

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.quram.common.datasources.MadaniV1DataSource
import org.quram.common.datasources.QuranDataSource
import org.quram.common.repositories.SurahRepository
import org.quram.common.utils.QuranDisplayData
import org.quram.common.utils.QuranInfo
import org.quram.common.utils.QuranPageInfo
import org.quram.common.utils.QuranPageInfoImpl
import org.quram.common.utils.Rub3DisplayUseCase

val CommonModule = module {

    factoryOf(::MadaniV1DataSource) bind QuranDataSource::class

    factoryOf(::QuranPageInfoImpl) bind QuranPageInfo::class

    factoryOf(::QuranDisplayData)

    factoryOf(::Rub3DisplayUseCase)

    singleOf(::QuranInfo)

    factoryOf(::SurahRepository)
}