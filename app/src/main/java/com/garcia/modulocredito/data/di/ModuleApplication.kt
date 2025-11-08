package com.garcia.modulocredito.data.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ModuleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ModuleApplication)
            modules(appModule)
        }
    }
}
