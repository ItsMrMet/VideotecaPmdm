package com.thomasvaneemeren.videotecapmdm.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thomasvaneemeren.videotecapmdm.data.database.entities.UserFavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserFavoriteDao {
    @Query("SELECT * FROM user_favorites WHERE userId = :userId AND movieId = :movieId")
    fun isFavorite(userId: String, movieId: Int): Flow<UserFavoriteEntity?>

    @Query("SELECT movieId FROM user_favorites WHERE userId = :userId")
    fun getFavoriteMovieIds(userId: String): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: UserFavoriteEntity)

    @Delete
    suspend fun delete(favorite: UserFavoriteEntity)
}