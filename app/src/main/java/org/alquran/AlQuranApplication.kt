package org.alquran

import android.app.Application
import org.alquran.ui.UiModule
import org.alquran.usecases.UseCaseModule
import org.alquran.verses.VersesModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.muslimapp.core.audio.PlaybackModule
import org.quram.common.CommonModule
import org.quran.bookmarks.BookmarkModule
import org.quran.datastore.di.DataStoreModule
import org.quran.domain.quran.QuranDomainModule
import org.quran.network.NetworkModule
import org.quran.translation.TranslationModule
import timber.log.Timber

class AlQuranApplication : Application(), KoinComponent {

  override fun onCreate() {
    super.onCreate()
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
    startKoin {
      androidContext(this@AlQuranApplication)
      workManagerFactory()
      androidLogger()

      modules(
        DataStoreModule,
        TranslationModule,
        NetworkModule,
        PlaybackModule,
        CommonModule,
        UiModule,
        VersesModule,
        UseCaseModule,
        QuranDomainModule,
        BookmarkModule,
      )
    }
  }
}