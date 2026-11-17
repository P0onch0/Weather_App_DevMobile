package com.legaut.devmobile

import android.app.Application
import com.legaut.devmobile.di.initKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}
