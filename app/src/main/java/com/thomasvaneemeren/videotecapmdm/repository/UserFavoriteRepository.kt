package com.thomasvaneemeren.videotecapmdm.repository

import kotlinx.coroutines.flow.Flow

interface UserFavoriteRepository {
    fun isFavorite(userId: String, movieId: Int): Flow<Boolean>
    fun getFavoriteMovieIds(userId: String): Flow<List<Int>>
    suspend fun setFavorite(userId: String, movieId: Int, isFavorite: Boolean)
}
