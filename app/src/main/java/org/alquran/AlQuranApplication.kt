package org.alquran

import android.app.Application
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import org.alquran.hafs.HafsModule
import org.alquran.ui.UiModule
import org.alquran.usecases.UseCaseModule
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
import org.quran.domain.quran.workers.SyncWorker
import org.quran.domain.quran.workers.WorkerModule
import org.quran.network.NetworkModule
import org.quran.translation.TranslationModule
import timber.log.Timber

class AlQuranApplication : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger()
            androidContext(this@AlQuranApplication)
            workManagerFactory()

            modules(
                DataStoreModule,
                TranslationModule,
                NetworkModule,
                PlaybackModule,
                CommonModule,
                UiModule,
                HafsModule,
                UseCaseModule,
                WorkerModule,
                QuranDomainModule,
                BookmarkModule,
            )
        }

        WorkManager.getInstance(this)
            .enqueue(OneTimeWorkRequestBuilder<SyncWorker>().build())
    }
}