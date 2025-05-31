package com.thomasvaneemeren.videotecapmdm.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thomasvaneemeren.videotecapmdm.data.datastore.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPreferencesViewModel @Inject constructor(
    private val repository: UserPreferencesRepository
) : ViewModel() {

    val userName: StateFlow<String> = repository.userNameFlow
        .map { it ?: "" }
        .stateIn(viewModelScope, SharingStarted.Lazily, "")

    val userRole: StateFlow<String> = repository.userRoleFlow
        .map { it ?: "user" }
        .stateIn(viewModelScope, SharingStarted.Lazily, "user")

    val isAdmin: StateFlow<Boolean> = userRole
        .map { it.equals("admin", ignoreCase = true) }
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun login(name: String) {
        val normalized = name.trim().lowercase()
        viewModelScope.launch {
            repository.setUserName(normalized)
            val role = if (normalized == "thomas") "admin" else "user"
            repository.setUserRole(role)
        }
    }
}



