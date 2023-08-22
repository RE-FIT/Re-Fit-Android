package com.example.refit

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.refit.BuildConfig.NATIVE_APP_KEY
import com.example.refit.data.workmanager.KoinWorkerFactory
import com.example.refit.di.dataSourceModule
import com.example.refit.di.dataStoreModule
import com.example.refit.di.networkModule
import com.example.refit.di.networkNodeModule
import com.example.refit.di.repositoryModule
import com.example.refit.di.viewModelModule
import com.kakao.sdk.common.KakaoSdk
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.KoinContextHandler
import org.koin.core.context.startKoin
import timber.log.Timber

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(networkModule, networkNodeModule, viewModelModule, repositoryModule, dataSourceModule, dataStoreModule)
        }

        KakaoSdk.init(this, NATIVE_APP_KEY)

        setUpTimber()
    }

    private fun setUpTimber() {
        Timber.plant(Timber.DebugTree())
    }
}