package com.bakulabs.twittervideodownloader

import android.app.Application
import timber.log.Timber

class DownloaderApplication: Application() {
    val appContainer: AppContainer by lazy {
        AppContainer(applicationContext.contentResolver)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}