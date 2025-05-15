package com.thomasvaneemeren.videotecapmdm.data.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.InternalSerializationApi

class UserPreferencesRepository @OptIn(InternalSerializationApi::class) constructor(
    private val dataStore: DataStore<UserPreferences>
) {
    @OptIn(InternalSerializationApi::class)
    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data

    @OptIn(InternalSerializationApi::class)
    suspend fun saveUsername(username: String) {
        dataStore.updateData { currentPreferences ->
            currentPreferences.copy(username = username, isOnboardingCompleted = true)
        }
    }
}