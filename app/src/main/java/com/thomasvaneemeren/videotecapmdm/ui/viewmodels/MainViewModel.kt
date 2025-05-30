package com.thomasvaneemeren.videotecapmdm.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thomasvaneemeren.videotecapmdm.data.datastore.UserPreferencesRepository
import com.thomasvaneemeren.videotecapmdm.data.database.DatabaseFactory
import com.thomasvaneemeren.videotecapmdm.data.entities.MovieEntity
import com.thomasvaneemeren.videotecapmdm.repository.MovieRepositoryImpl
import com.thomasvaneemeren.videotecapmdm.repository.UserFavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class MainViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userFavoriteRepository: UserFavoriteRepository,
    private val databaseFactory: DatabaseFactory
) : ViewModel() {

    private val _userName = MutableStateFlow<String?>(null)
    val userName: StateFlow<String?> = _userName.asStateFlow()

    private val _favoritesOnly = MutableStateFlow(false)
    val favoritesOnly: StateFlow<Boolean> = _favoritesOnly.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    private val allMovies: StateFlow<List<MovieEntity>> = _userName
        .filterNotNull()
        .flatMapLatest {
            val db = databaseFactory.createDatabase()
            val repo = MovieRepositoryImpl(db.movieDao())
            repo.getAllMovies()
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val favoriteIdsFlow: Flow<Set<Int>> = _userName
        .filterNotNull()
        .flatMapLatest { user ->
            userFavoriteRepository.getFavoriteMovieIds(user).map { it.toSet() }
        }

    val movies: StateFlow<List<MovieEntity>> = combine(
        allMovies,
        _favoritesOnly,
        favoriteIdsFlow,
        _searchQuery
    ) { movies, favoritesOnly, favoriteIds, query ->
        var filtered = movies
        if (favoritesOnly) {
            filtered = filtered.filter { it.id in favoriteIds }
        }
        if (query.isNotBlank()) {
            filtered = filtered.filter { it.title.contains(query, ignoreCase = true) }
        }
        filtered
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        viewModelScope.launch {
            userPreferencesRepository.userNameFlow.collect {
                _userName.value = it?.lowercase() // <-- NORMALIZA también aquí
            }
        }
    }

    fun toggleFavorite(movieId: Int) {
        viewModelScope.launch {
            val userId = _userName.value ?: return@launch
            val isFav = userFavoriteRepository.isFavorite(userId, movieId)
            userFavoriteRepository.setFavorite(userId, movieId, !isFav)
        }
    }

    fun logout() = viewModelScope.launch {
        userPreferencesRepository.clearUserData()
    }

    fun toggleFavoritesOnly() {
        _favoritesOnly.value = !_favoritesOnly.value
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
