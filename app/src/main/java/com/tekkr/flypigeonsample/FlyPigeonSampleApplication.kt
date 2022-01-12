package com.tekkr.flypigeonsample

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class FlyPigeonSampleApplication: Application() {
    companion object{
        var appContext: FlyPigeonSampleApplication? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}