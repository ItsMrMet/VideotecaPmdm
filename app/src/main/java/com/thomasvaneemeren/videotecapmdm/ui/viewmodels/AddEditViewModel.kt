package com.thomasvaneemeren.videotecapmdm.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thomasvaneemeren.videotecapmdm.data.database.DatabaseFactory
import com.thomasvaneemeren.videotecapmdm.data.datastore.UserPreferencesRepository
import com.thomasvaneemeren.videotecapmdm.data.entities.MovieEntity
import com.thomasvaneemeren.videotecapmdm.data.repository.MovieRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val databaseFactory: DatabaseFactory
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
            val repo = getRepository() ?: return@launch

            val current = _movie.value
            if (current == null) {
                val newMovie = MovieEntity(
                    id = 0,
                    title = title,
                    genre = genre,
                    synopsis = synopsis,
                    duration = duration,
                    director = director,
                    isFavorite = isFavorite,
                    userId = user
                )
                repo.insertMovie(newMovie)
            } else {
                val updated = current.copy(
                    title = title,
                    genre = genre,
                    synopsis = synopsis,
                    duration = duration,
                    director = director,
                    isFavorite = isFavorite
                )
                repo.updateMovie(updated)
            }
        }
    }
}

