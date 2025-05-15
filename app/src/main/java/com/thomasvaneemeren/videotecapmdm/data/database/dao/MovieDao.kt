package com.thomasvaneemeren.videotecapmdm.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.thomasvaneemeren.videotecapmdm.model.Movie
import com.thomasvaneemeren.videotecapmdm.model.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert
    suspend fun insertMovie(movie: Movie)

    @Update
    suspend fun updateMovie(movie: Movie)

    @Delete
    suspend fun deleteMovie(movie: Movie)

    @Query("SELECT * FROM movies WHERE userId = :userId")
    fun getAllMovies(userId: String): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE id = :id AND userId = :userId")
    suspend fun getMovieById(id: Int, userId: String): MovieEntity?

    @Query("SELECT * FROM movies WHERE title LIKE :query AND userId = :userId")
    fun searchMovies(query: String, userId: String): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE isFavorite = 1 AND userId = :userId")
    fun getFavoriteMovies(userId: String): Flow<List<MovieEntity>>
}