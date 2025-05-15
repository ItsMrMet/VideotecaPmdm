package com.thomasvaneemeren.videotecapmdm.data.repository

import com.thomasvaneemeren.videotecapmdm.data.database.dao.MovieDao
import com.thomasvaneemeren.videotecapmdm.model.Movie
import com.thomasvaneemeren.videotecapmdm.model.MovieEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
abstract class MovieRepositoryImpl @Inject constructor(
    private val movieDao: MovieDao
) : MovieRepository {

    override fun getAllMovies(userId: String): Flow<List<MovieEntity>> = movieDao.getAllMovies()

    suspend fun insertMovie(movie: Movie) = movieDao.insertMovie(movie)

    suspend fun updateMovie(movie: Movie) = movieDao.updateMovie(movie)

    suspend fun deleteMovie(movie: Movie) = movieDao.deleteMovie(movie)

    suspend fun getMovieById(id: Int): Movie? = movieDao.getMovieById(id) as Movie?
}
