package com.thomasvaneemeren.videotecapmdm.data.repository

import com.thomasvaneemeren.videotecapmdm.data.database.dao.UserFavoriteDao
import com.thomasvaneemeren.videotecapmdm.data.entities.UserFavoriteEntity
import com.thomasvaneemeren.videotecapmdm.repository.UserFavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserFavoriteRepositoryImpl(
    private val dao: UserFavoriteDao
) : UserFavoriteRepository {

    override fun isFavorite(userId: String, movieId: Int): Flow<Boolean> {
        return dao.existsFavorite(userId, movieId).map { it != null }
    }

    override fun getFavoriteMovieIds(userId: String): Flow<List<Int>> {
        return dao.getFavoriteMovieIdsByUser(userId)
    }

    override suspend fun setFavorite(userId: String, movieId: Int, isFavorite: Boolean) {
        val favorite = UserFavoriteEntity(userId, movieId)
        if (isFavorite) {
            dao.addFavorite(favorite)
        } else {
            dao.removeFavorite(favorite)
        }
    }
}
