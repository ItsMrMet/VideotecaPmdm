package com.thomasvaneemeren.videotecapmdm.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val USERNAME_KEY = stringPreferencesKey("username")
    }

    val usernameFlow: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[USERNAME_KEY]
        }

    suspend fun saveUsername(username: String) {
        dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = username
        }
    }

    // Para obtener el username en forma suspend (puedes usarlo para crear DB)
    suspend fun getUsername(): String? {
        return dataStore.data.map { it[USERNAME_KEY] }.firstOrNull()
    }

    suspend fun clearUser() {
        dataStore.edit { it.clear() }
    }
}
