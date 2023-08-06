package com.example.refit

import android.app.Application
import com.example.refit.di.dataSourceModule
import com.example.refit.di.dataStoreModule
import com.example.refit.di.networkModule
import com.example.refit.di.networkNodeModule
import com.example.refit.di.repositoryModule
import com.example.refit.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(networkModule, networkNodeModule, viewModelModule, repositoryModule, dataSourceModule, dataStoreModule)
        }

        setUpTimber()
    }

    private fun setUpTimber() {
        Timber.plant(Timber.DebugTree())
    }
}