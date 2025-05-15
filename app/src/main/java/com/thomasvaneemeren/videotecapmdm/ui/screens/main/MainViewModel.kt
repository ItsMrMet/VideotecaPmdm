package com.thomasvaneemeren.videotecapmdm.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thomasvaneemeren.videotecapmdm.data.datastore.UserPreferencesRepository
import com.thomasvaneemeren.videotecapmdm.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userPreferences: UserPreferencesRepository,
    private val movieRepository: MovieRepository
) : ViewModel() {

    val movies = movieRepository.getAllMovies()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val usernameFlow = userPreferences.usernameFlow

    fun logout() = viewModelScope.launch {
        userPreferences.clearUser()
    }
}
