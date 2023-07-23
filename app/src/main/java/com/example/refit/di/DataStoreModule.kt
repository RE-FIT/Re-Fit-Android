package com.example.refit.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.refit.data.datastore.TokenStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_token")

val dataStoreModule = module {
    single { androidContext().dataStore }
    single { TokenStore(get()) }
}