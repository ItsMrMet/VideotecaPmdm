package com.thomasvaneemeren.videotecapmdm.repository

import com.thomasvaneemeren.videotecapmdm.data.database.dao.UserFavoriteDao
import com.thomasvaneemeren.videotecapmdm.data.entities.UserFavoriteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class UserFavoriteRepositoryImpl @Inject constructor(
    private val userFavoriteDao: UserFavoriteDao
) : UserFavoriteRepository {

    override suspend fun isFavorite(userId: String, movieId: Int): Boolean {
        return userFavoriteDao.isFavorite(userId, movieId).firstOrNull() != null
    }

    override fun getFavoriteMovieIds(userId: String): Flow<List<Int>> {
        return userFavoriteDao.getFavoriteMovieIds(userId)
    }

    override suspend fun setFavorite(userId: String, movieId: Int, isFavorite: Boolean) {
        val favorite = UserFavoriteEntity(userId = userId, movieId = movieId)
        if (isFavorite) {
            userFavoriteDao.insert(favorite)
        } else {
            userFavoriteDao.delete(favorite)
        }
    }
}
