// En data/repository/MovieRepositoryImpl.kt
package com.thomasvaneemeren.videotecapmdm.data.repository

import com.thomasvaneemeren.videotecapmdm.data.database.dao.MovieDao
import com.thomasvaneemeren.videotecapmdm.model.Genre
import com.thomasvaneemeren.videotecapmdm.model.Movie
import com.thomasvaneemeren.videotecapmdm.model.MovieEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieDao: MovieDao
) : MovieRepository {

    override fun getAllMovies(userId: String): Flow<List<Movie>> {
        return movieDao.getAllMovies(userId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getMoviesByUser(userId: String): Flow<List<Movie>> {
        return getAllMovies(userId)
    }

    override suspend fun insert(movie: Movie) {
        movieDao.insertMovie(movie.toEntity())
    }

    override suspend fun update(movie: Movie) {
        movieDao.updateMovie(movie.toEntity())
    }

    override suspend fun delete(movie: Movie) {
        movieDao.deleteMovie(movie.toEntity())
    }

    private fun MovieEntity.toDomainModel(): Movie {
        return Movie(
            id = this.id,
            title = this.title,
            genre = Genre.valueOf(this.genre),
            synopsis = this.synopsis,
            duration = this.duration,
            director = this.director,
            isFavorite = this.isFavorite,
            userId = this.userId
        )
    }

    private fun Movie.toEntity(): MovieEntity {
        return MovieEntity(
            id = this.id,
            title = this.title,
            genre = this.genre.name,
            synopsis = this.synopsis,
            duration = this.duration,
            director = this.director,
            isFavorite = this.isFavorite,
            userId = this.userId
        )
    }
}