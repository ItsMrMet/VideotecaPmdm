package com.thomasvaneemeren.videotecapmdm.data.database.dao

import androidx.room.*
import com.thomasvaneemeren.videotecapmdm.data.entities.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies WHERE userId = :userId")
    fun getMoviesByUser(userId: String): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE id = :id AND userId = :userId LIMIT 1")
    suspend fun getMovieById(id: Int, userId: String): MovieEntity?

    @Query("SELECT * FROM movies WHERE id = :id AND userId = :userId LIMIT 1")
    fun getMovieByIdFlow(id: Int, userId: String): Flow<MovieEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieEntity)

    @Update
    suspend fun update(movie: MovieEntity)

    @Delete
    suspend fun delete(movie: MovieEntity)
}
