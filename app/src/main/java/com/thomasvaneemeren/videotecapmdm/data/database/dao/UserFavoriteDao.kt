package com.thomasvaneemeren.videotecapmdm.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thomasvaneemeren.videotecapmdm.data.entities.UserFavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserFavoriteDao {

    @Query("SELECT movieId FROM user_favorites WHERE userId = :userId")
    fun getFavoriteMovieIdsByUser(userId: String): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: UserFavoriteEntity)

    @Delete
    suspend fun removeFavorite(favorite: UserFavoriteEntity)

    @Query("DELETE FROM user_favorites WHERE userId = :userId")
    suspend fun clearFavoritesForUser(userId: String)

    @Query("SELECT * FROM user_favorites WHERE userId = :userId AND movieId = :movieId LIMIT 1")
    fun existsFavorite(userId: String, movieId: Int): Flow<UserFavoriteEntity?>
}

