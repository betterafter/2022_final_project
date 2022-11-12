package com.example.kudata

import android.app.Application

open class App : Application()  {
    private var instance: App? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}