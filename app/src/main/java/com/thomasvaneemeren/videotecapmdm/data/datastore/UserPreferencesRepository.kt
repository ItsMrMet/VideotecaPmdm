package com.thomasvaneemeren.videotecapmdm.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val USER_NAME_KEY = stringPreferencesKey("user_name")
private val USER_ROLE_KEY = stringPreferencesKey("user_role")

@Singleton
class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    val userNameFlow: Flow<String?> = dataStore.data.map { it[USER_NAME_KEY] }
    val userRoleFlow: Flow<String?> = dataStore.data.map { it[USER_ROLE_KEY] }

    suspend fun setUserName(userName: String) {
        dataStore.edit { it[USER_NAME_KEY] = userName }
    }

    suspend fun setUserRole(role: String) {
        dataStore.edit { it[USER_ROLE_KEY] = role }
    }

    suspend fun clearUserData() {
        dataStore.edit {
            it.remove(USER_NAME_KEY)
            it.remove(USER_ROLE_KEY)
        }
    }

    suspend fun getUserName(): String? = userNameFlow.first()

    suspend fun getUserRole(): String? = userRoleFlow.first()

    suspend fun isAdmin(): Boolean {
        val role = getUserRole() ?: return false
        return role.equals("admin", ignoreCase = true)
    }

}