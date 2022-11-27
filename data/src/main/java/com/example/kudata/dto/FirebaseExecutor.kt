package com.example.kudata.dto

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

class FirebaseExecutor: Executor {
    private val threadLocalHandler = object : ThreadLocal<Handler>() {
        override fun initialValue(): Handler? {
            var looper = Looper.myLooper()
            if (looper == null)
                looper = Looper.getMainLooper()
            return looper?.let { Handler(it) }
        }
    }

    private val handler = threadLocalHandler.get()
    override fun execute(command: Runnable?) {
        if (command != null) {
            handler?.post(command)
        }
    }
}