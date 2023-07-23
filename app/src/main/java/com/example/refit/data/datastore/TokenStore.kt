package com.example.refit.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class TokenStore(private val dataStore: DataStore<Preferences>) {

    private val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")

    suspend fun setAccessToken(token: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = token
        }
    }

    suspend fun getAccessToken(): Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    exception.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[ACCESS_TOKEN] ?: ""
            }
    }

    suspend fun deleteAccessToken() {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = ""
        }
    }
}