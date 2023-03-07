package org.alquran.ui

import org.alquran.ui.screen.audioSheet.PlaybackSheetViewModel
import org.alquran.ui.screen.home.HomeViewModel
import org.alquran.ui.screen.pager.QuranPagerViewModel
import org.alquran.ui.screen.recitations.RecitationsViewModel
import org.alquran.ui.screen.reciters.RecitersViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val UiModule = module {

  viewModelOf(::HomeViewModel)

  viewModelOf(::PlaybackSheetViewModel)

  viewModelOf(::QuranPagerViewModel)

  viewModelOf(::RecitationsViewModel)

  viewModelOf(::RecitersViewModel)
}