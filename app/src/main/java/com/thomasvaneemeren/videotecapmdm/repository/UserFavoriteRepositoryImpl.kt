package com.thomasvaneemeren.videotecapmdm.repository

import com.thomasvaneemeren.videotecapmdm.data.database.dao.UserFavoriteDao
import com.thomasvaneemeren.videotecapmdm.data.database.entities.UserFavoriteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class UserFavoriteRepositoryImpl @Inject constructor(
    private val userFavoriteDao: UserFavoriteDao
) : UserFavoriteRepository {

    override suspend fun isFavorite(userId: String, movieId: Int): Boolean {
        return userFavoriteDao.isFavorite(userId.lowercase(), movieId).firstOrNull() != null
    }

    override fun getFavoriteMovieIds(userId: String): Flow<List<Int>> {
        return userFavoriteDao.getFavoriteMovieIds(userId.lowercase())
    }

    override suspend fun setFavorite(userId: String, movieId: Int, isFavorite: Boolean) {
        val normalizedUser = userId.lowercase()
        val favorite = UserFavoriteEntity(userId = normalizedUser, movieId = movieId)
        if (isFavorite) {
            userFavoriteDao.insert(favorite)
        } else {
            userFavoriteDao.delete(favorite)
        }
    }
}