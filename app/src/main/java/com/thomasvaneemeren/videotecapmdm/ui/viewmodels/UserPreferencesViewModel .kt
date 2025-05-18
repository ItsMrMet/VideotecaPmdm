package com.thomasvaneemeren.videotecapmdm.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thomasvaneemeren.videotecapmdm.data.datastore.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPreferencesViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val userName: StateFlow<String?> = userPreferencesRepository.userNameFlow.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        null
    )

    fun saveUserName(name: String) = viewModelScope.launch {
        userPreferencesRepository.saveUserName(name)
    }

    fun clearUserName() = viewModelScope.launch {
        userPreferencesRepository.clearUserName()
    }
}

