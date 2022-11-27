package com.kuroutine.kulture

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application()  {
    private var instance: App? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}