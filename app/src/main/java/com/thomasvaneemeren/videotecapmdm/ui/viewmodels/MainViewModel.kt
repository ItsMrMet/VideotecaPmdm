package com.thomasvaneemeren.videotecapmdm.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thomasvaneemeren.videotecapmdm.data.datastore.UserPreferencesRepository
import com.thomasvaneemeren.videotecapmdm.data.database.DatabaseFactory
import com.thomasvaneemeren.videotecapmdm.data.entities.MovieEntity
import com.thomasvaneemeren.videotecapmdm.data.repository.MovieRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val databaseFactory: DatabaseFactory
) : ViewModel() {

    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName.asStateFlow()

    private val _movies = MutableStateFlow<List<MovieEntity>>(emptyList())
    val movies: StateFlow<List<MovieEntity>> = _movies.asStateFlow()

    private val _favoritesOnly = MutableStateFlow(false)
    val favoritesOnly: StateFlow<Boolean> = _favoritesOnly

    private var currentMoviesJob: Job? = null

    init {
        viewModelScope.launch {
            userPreferencesRepository.userNameFlow.collect { user ->
                _userName.value = user
                currentMoviesJob?.cancel()
                if (user != null) {
                    val db = databaseFactory.createDatabase(user)
                    val dao = db.movieDao()
                    val repo = MovieRepositoryImpl(dao)
                    currentMoviesJob = launch {
                        repo.getMoviesByUser(user).collect { list ->
                            _movies.value = list
                        }
                    }
                } else {
                    _movies.value = emptyList()
                }
            }
        }
    }

    fun logout() = viewModelScope.launch {
        userPreferencesRepository.clearUserName()
        _movies.value = emptyList()
    }

    fun toggleFavoritesOnly() {
        _favoritesOnly.value = !_favoritesOnly.value
    }

    fun filterMovies(query: String) {
        val allMovies = _movies.value
        _movies.value = if (query.isBlank()) allMovies else allMovies.filter {
            it.title.contains(query, ignoreCase = true)
        }
    }
}



