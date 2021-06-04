package com.example.savvologytask

import android.app.Application
import com.example.savvologytask.di.coreModule
import com.example.savvologytask.di.networkModule
import com.example.savvologytask.di.vmModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MovieApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MovieApplication)
            modules(listOf(networkModule, vmModule, coreModule))
        }
    }

}