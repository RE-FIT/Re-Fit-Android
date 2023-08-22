package com.example.refit.di

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.refit.data.datastore.NotificationStore
import com.example.refit.data.datastore.Notifications
import com.example.refit.data.datastore.TokenStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.InputStream
import java.io.OutputStream

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_token")

object NotificationsSerializer : Serializer<Notifications> {

    override val defaultValue: Notifications
        get() = Notifications.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Notifications {
        try {
            return Notifications.parseFrom(input)
        } catch (exception: Exception) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: Notifications, output: OutputStream) {
        t.writeTo(output)
    }
}

val Context.notificationStore: DataStore<Notifications> by dataStore(
    fileName = "notifications.pb",
    serializer = NotificationsSerializer
)

val PreferenceDataStore = named("dateStore")
val NotificationDataStore = named("notificationStore")

val dataStoreModule = module {
    single(PreferenceDataStore) { androidContext().dataStore }
    single(NotificationDataStore) { androidContext().notificationStore }

    single { TokenStore(get(PreferenceDataStore)) }
    single { NotificationStore(get(NotificationDataStore)) }
}