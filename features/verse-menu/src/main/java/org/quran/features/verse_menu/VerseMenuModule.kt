package org.quran.features.verse_menu

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import org.quran.bookmarks.BookmarkModule
import org.quran.core.audio.AudioCoreModule

val FeatureVerseMenuModule = module {
  includes(BookmarkModule, AudioCoreModule)

  viewModelOf(::VerseMenuViewModel)
}