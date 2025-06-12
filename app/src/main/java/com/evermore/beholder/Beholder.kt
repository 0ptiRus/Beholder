package com.evermore.beholder

import android.app.Application
import com.evermore.beholder.di.domainModule
import com.evermore.beholder.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class Beholder : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@Beholder)
            modules(viewModelModule, domainModule)
        }
    }
}