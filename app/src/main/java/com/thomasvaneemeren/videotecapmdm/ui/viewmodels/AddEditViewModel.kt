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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
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

    private suspend fun getRepository(): MovieRepositoryImpl? {
        val user = userPreferencesRepository.getUserName() ?: return null
        val db = databaseFactory.createDatabase(user)
        return MovieRepositoryImpl(db.movieDao())
    }

    fun loadMovie(id: Int) {
        viewModelScope.launch {
            val repo = getRepository() ?: return@launch
            val user = userPreferencesRepository.getUserName() ?: return@launch
            _movie.value = repo.getMovieById(id, user)
        }
    }

    fun isFavorite(movieId: Int): Flow<Boolean> = flow {
        val user = userPreferencesRepository.getUserName() ?: return@flow
        emit(userFavoriteRepository.isFavorite(user, movieId))
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
            val db = databaseFactory.createDatabase(user)
            val dao = db.movieDao()

            val currentMovie = movie.value
            if (currentMovie != null) {
                // Editar película existente
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
                // Insertar nueva película
                val newMovie = MovieEntity(
                    title = title,
                    genre = genre,
                    synopsis = synopsis,
                    duration = duration,
                    director = director,
                    userId = user
                )
                val id = dao.insert(newMovie)

                // Guardar favorito si es necesario
                if (isFavorite && id > 0) {
                    userFavoriteRepository.setFavorite(user, id.toInt(), true)
                }
            }
        }
    }

}

