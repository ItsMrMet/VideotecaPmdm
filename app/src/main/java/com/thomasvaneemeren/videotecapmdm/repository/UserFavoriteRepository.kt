package com.thomasvaneemeren.videotecapmdm.repository

import kotlinx.coroutines.flow.Flow

interface UserFavoriteRepository {
    suspend fun isFavorite(userId: String, movieId: Int): Boolean
    fun getFavoriteMovieIds(userId: String): Flow<List<Int>>
    suspend fun setFavorite(userId: String, movieId: Int, isFavorite: Boolean)
}
