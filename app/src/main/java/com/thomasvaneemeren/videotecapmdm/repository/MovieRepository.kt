package com.thomasvaneemeren.videotecapmdm.repository

import com.thomasvaneemeren.videotecapmdm.data.model.Movie
import com.thomasvaneemeren.videotecapmdm.data.model.MovieEntity
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getAllMovies(userId: String): Flow<List<MovieEntity>>
    fun getMoviesByUser(userId: String): Flow<List<Movie>>
    suspend fun insert(movie: Movie)
    suspend fun update(movie: Movie)
    suspend fun delete(movie: Movie)
}