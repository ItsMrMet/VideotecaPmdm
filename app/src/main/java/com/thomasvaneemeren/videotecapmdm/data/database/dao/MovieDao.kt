package com.thomasvaneemeren.videotecapmdm.data.database.dao

import androidx.room.*
import com.thomasvaneemeren.videotecapmdm.data.entities.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies WHERE userId = :userId ORDER BY title ASC")
    fun getAllMovies(userId: String): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE id = :id AND userId = :userId LIMIT 1")
    suspend fun getMovieById(id: Int, userId: String): MovieEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Update
    suspend fun updateMovie(movie: MovieEntity)

    @Delete
    suspend fun deleteMovie(movie: MovieEntity)
}
