package com.thomasvaneemeren.videotecapmdm.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thomasvaneemeren.videotecapmdm.data.datastore.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.InternalSerializationApi
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userPreferences: UserPreferencesRepository
) : ViewModel() {
    private val _navigateTo = MutableStateFlow("")
    val navigateTo: StateFlow<String> = _navigateTo

    @OptIn(InternalSerializationApi::class)
    fun onSplashShown() = viewModelScope.launch {
        userPreferences.userPreferencesFlow.collect { prefs ->
            _navigateTo.value = if (prefs.isOnboardingCompleted) "main" else "onboarding"
        }
    }
}