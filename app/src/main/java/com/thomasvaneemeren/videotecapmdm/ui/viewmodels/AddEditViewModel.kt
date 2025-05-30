package com.thomasvaneemeren.videotecapmdm.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thomasvaneemeren.videotecapmdm.data.database.DatabaseFactory
import com.thomasvaneemeren.videotecapmdm.data.datastore.UserPreferencesRepository
import com.thomasvaneemeren.videotecapmdm.data.entities.MovieEntity
import com.thomasvaneemeren.videotecapmdm.repository.MovieRepositoryImpl
import com.thomasvaneemeren.videotecapmdm.repository.UserFavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val databaseFactory: DatabaseFactory,
    private val userFavoriteRepository: UserFavoriteRepository
) : ViewModel() {

    private val _movie = MutableStateFlow<MovieEntity?>(null)
    val movie: StateFlow<MovieEntity?> = _movie

    private suspend fun getRepository(): MovieRepositoryImpl {
        return MovieRepositoryImpl(databaseFactory.createDatabase().movieDao())
    }

    fun loadMovie(id: Int) {
        viewModelScope.launch {
            val repo = getRepository()
            _movie.value = repo.getMovieById(id)
        }
    }

    fun isFavorite(movieId: Int): Flow<Boolean> = flow {
        val user = userPreferencesRepository.getUserName() ?: return@flow
        emit(userFavoriteRepository.isFavorite(user, movieId))
    }

    suspend fun isAdmin(): Boolean {
        return userPreferencesRepository.isAdmin()
    }

    fun deleteMovie(onDeleted: () -> Unit) {
        viewModelScope.launch {
            if (!userPreferencesRepository.isAdmin()) return@launch
            val movieToDelete = _movie.value ?: return@launch
            val repo = getRepository()
            repo.deleteMovie(movieToDelete)
            onDeleted()
        }
    }

    fun saveMovie(
        title: String,
        genre: String,
        synopsis: String,
        duration: Int,
        director: String,
        isFavorite: Boolean
    ) {
        viewModelScope.launch {
            val user = userPreferencesRepository.getUserName() ?: return@launch
            val db = databaseFactory.createDatabase()
            val dao = db.movieDao()

            val currentMovie = movie.value
            if (currentMovie != null) {
                val updated = currentMovie.copy(
                    title = title,
                    genre = genre,
                    synopsis = synopsis,
                    duration = duration,
                    director = director
                )
                dao.update(updated)
                userFavoriteRepository.setFavorite(user, updated.id, isFavorite)
            } else {
                val newMovie = MovieEntity(
                    title = title,
                    genre = genre,
                    synopsis = synopsis,
                    duration = duration,
                    director = director
                )
                val id = dao.insert(newMovie)
                if (isFavorite && id > 0) {
                    userFavoriteRepository.setFavorite(user, id.toInt(), true)
                }
            }
        }
    }
}
