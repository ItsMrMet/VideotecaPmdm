package com.thomasvaneemeren.videotecapmdm.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thomasvaneemeren.videotecapmdm.data.repository.MovieRepository
import com.thomasvaneemeren.videotecapmdm.data.datastore.UserPreferencesRepository
import com.thomasvaneemeren.videotecapmdm.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.InternalSerializationApi
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userPreferences: UserPreferencesRepository,
    private val movieRepository: MovieRepository
) : ViewModel() {

    @OptIn(InternalSerializationApi::class)
    val username: StateFlow<String> = userPreferences.userPreferencesFlow
        .map { it.username }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    @OptIn(ExperimentalCoroutinesApi::class, InternalSerializationApi::class)
    val movies: StateFlow<List<Movie>> = userPreferences.userPreferencesFlow // Cambiado a List<Movie>
        .flatMapLatest { prefs ->
            movieRepository.getMoviesByUser(prefs.username)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    suspend fun deleteMovie(movie: Movie) {
        movieRepository.delete(movie)
    }
}