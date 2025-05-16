package com.thomasvaneemeren.videotecapmdm.repository

import com.thomasvaneemeren.videotecapmdm.data.entities.MovieEntity
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMoviesByUser(userId: String): Flow<List<MovieEntity>>
    fun getMovieByIdFlow(id: Int, userId: String): Flow<MovieEntity?>
    suspend fun insertMovie(movie: MovieEntity)
    suspend fun updateMovie(movie: MovieEntity)
    suspend fun deleteMovie(movie: MovieEntity)
    suspend fun getMovieById(id: Int, userId: String): MovieEntity?
}
