package com.example.refit

import android.app.Application
import com.example.refit.di.dataSourceModule
import com.example.refit.di.networkModule
import com.example.refit.di.repositoryModule
import com.example.refit.di.viewModelModule
import org.koin.core.context.startKoin
import timber.log.Timber

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(networkModule,  viewModelModule, repositoryModule, dataSourceModule)
        }

        setUpTimber()
    }

    private fun setUpTimber() {
        Timber.plant(Timber.DebugTree())
    }
}