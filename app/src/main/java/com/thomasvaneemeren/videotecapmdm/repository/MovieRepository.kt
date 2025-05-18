package com.thomasvaneemeren.videotecapmdm.repository

import com.thomasvaneemeren.videotecapmdm.data.entities.MovieEntity
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getAllMovies(): Flow<List<MovieEntity>>
    fun getMovieByIdFlow(id: Int): Flow<MovieEntity?>
    suspend fun insertMovie(movie: MovieEntity)
    suspend fun updateMovie(movie: MovieEntity)
    suspend fun deleteMovie(movie: MovieEntity)
    suspend fun getMovieById(id: Int): MovieEntity?
}

