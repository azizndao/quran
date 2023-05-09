package org.alquran.ui

import org.alquran.ui.screen.audioSheet.PlaybackSheetViewModel
import org.alquran.ui.screen.home.HomeViewModel
import org.alquran.ui.screen.pager.QuranPagerViewModel
import org.alquran.ui.screen.recitations.RecitationsViewModel
import org.alquran.ui.screen.reciters.RecitersViewModel
import org.alquran.ui.screen.search.QuranSearchViewModel
import org.alquran.ui.screen.translations.TranslationsViewModel
import org.alquran.ui.screen.verseMenu.VerseMenuViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val UiModule = module {

  viewModelOf(::HomeViewModel)

  viewModelOf(::PlaybackSheetViewModel)

  viewModelOf(::QuranPagerViewModel)

  viewModelOf(::QuranSearchViewModel)

  viewModelOf(::RecitationsViewModel)

  viewModelOf(::RecitersViewModel)

  viewModelOf(::VerseMenuViewModel)

  viewModelOf(::TranslationsViewModel)
}