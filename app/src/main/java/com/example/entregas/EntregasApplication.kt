package com.example.entregas

import android.app.Application
import com.example.entregas.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class EntregasApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@EntregasApplication)
            modules(
                appModules
            )
        }
    }
}