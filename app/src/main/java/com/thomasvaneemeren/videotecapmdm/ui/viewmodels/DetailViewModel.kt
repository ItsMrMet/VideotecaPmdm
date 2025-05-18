package com.thomasvaneemeren.videotecapmdm.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thomasvaneemeren.videotecapmdm.data.database.DatabaseFactory
import com.thomasvaneemeren.videotecapmdm.data.datastore.UserPreferencesRepository
import com.thomasvaneemeren.videotecapmdm.data.entities.MovieEntity
import com.thomasvaneemeren.videotecapmdm.data.repository.MovieRepositoryImpl
import com.thomasvaneemeren.videotecapmdm.repository.UserFavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val databaseFactory: DatabaseFactory,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userFavoriteRepository: UserFavoriteRepository
) : ViewModel() {

    fun getMovieById(movieId: Int): Flow<MovieEntity?> = flow {
        val user = userPreferencesRepository.getUserName() ?: return@flow
        val db = databaseFactory.createDatabase(user)
        val dao = db.movieDao()
        emit(dao.getMovieById(movieId, user))
    }

    fun isFavorite(movieId: Int): Flow<Boolean> = flow {
        val user = userPreferencesRepository.getUserName() ?: return@flow
        emitAll(userFavoriteRepository.isFavorite(user, movieId))
    }

    fun toggleFavorite(movieId: Int, favorite: Boolean) {
        viewModelScope.launch {
            val user = userPreferencesRepository.getUserName() ?: return@launch
            userFavoriteRepository.setFavorite(user, movieId, favorite)
        }
    }

    fun deleteMovie(movie: MovieEntity) {
        viewModelScope.launch {
            val user = userPreferencesRepository.getUserName() ?: return@launch
            val db = databaseFactory.createDatabase(user)
            val dao = db.movieDao()
            val repo = MovieRepositoryImpl(dao)
            repo.deleteMovie(movie.copy(userId = user))
        }
    }
}

