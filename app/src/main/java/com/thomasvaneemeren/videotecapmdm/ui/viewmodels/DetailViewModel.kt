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
    private val userPreferencesRepository: UserPreferencesRepository,
    private val databaseFactory: DatabaseFactory,
    private val userFavoriteRepository: UserFavoriteRepository
) : ViewModel() {

    fun getMovieById(id: Int): Flow<MovieEntity?> = flow {
        val user = userPreferencesRepository.getUserName() ?: return@flow
        val db = databaseFactory.createDatabase(user)
        val movie = db.movieDao().getMovieById(id, user)
        emit(movie)
    }

    fun isFavorite(movieId: Int): Flow<Boolean> = flow {
        val user = userPreferencesRepository.getUserName() ?: return@flow
        emit(userFavoriteRepository.isFavorite(user, movieId))
    }

    fun deleteMovie(movie: MovieEntity) {
        viewModelScope.launch {
            val user = userPreferencesRepository.getUserName() ?: return@launch
            val db = databaseFactory.createDatabase(user)
            db.movieDao().delete(movie)
            userFavoriteRepository.setFavorite(user, movie.id, false)
        }
    }
}


