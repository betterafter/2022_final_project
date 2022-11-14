package com.example.domain

import android.app.Application

open class App : Application()  {
    private var instance: App? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}