package com.thomasvaneemeren.videotecapmdm.repository

import com.thomasvaneemeren.videotecapmdm.data.database.dao.MovieDao
import com.thomasvaneemeren.videotecapmdm.data.entities.MovieEntity
import kotlinx.coroutines.flow.Flow

class MovieRepositoryImpl(
    private val movieDao: MovieDao
) : MovieRepository {
    override fun getAllMovies(): Flow<List<MovieEntity>> = movieDao.getAllMovies()
    override fun getMovieByIdFlow(id: Int): Flow<MovieEntity?> = movieDao.getMovieByIdFlow(id)
    override suspend fun getMovieById(id: Int): MovieEntity? = movieDao.getMovieById(id)
    override suspend fun insertMovie(movie: MovieEntity): Long = movieDao.insert(movie)
    override suspend fun updateMovie(movie: MovieEntity) = movieDao.update(movie)
    override suspend fun deleteMovie(movie: MovieEntity) = movieDao.delete(movie)
}
