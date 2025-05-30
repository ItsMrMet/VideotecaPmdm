package com.thomasvaneemeren.videotecapmdm.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thomasvaneemeren.videotecapmdm.data.database.DatabaseFactory
import com.thomasvaneemeren.videotecapmdm.data.datastore.UserPreferencesRepository
import com.thomasvaneemeren.videotecapmdm.data.entities.MovieEntity
import com.thomasvaneemeren.videotecapmdm.repository.UserFavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val databaseFactory: DatabaseFactory,
    private val userFavoriteRepository: UserFavoriteRepository
) : ViewModel() {

    private val db by lazy { databaseFactory.createDatabase() }

    fun getMovieById(id: Int): Flow<MovieEntity?> = flow {
        emit(db.movieDao().getMovieById(id))
    }

    fun isFavorite(movieId: Int): Flow<Boolean> = flow {
        val user = userPreferencesRepository.getUserName() ?: return@flow
        emit(userFavoriteRepository.isFavorite(user, movieId))
    }

    fun deleteMovie(movie: MovieEntity) {
        viewModelScope.launch {
            val user = userPreferencesRepository.getUserName()?.lowercase() ?: return@launch
            if (user != "thomas") return@launch

            db.movieDao().delete(movie)
            userFavoriteRepository.setFavorite(user, movie.id, false)
        }
    }

}

