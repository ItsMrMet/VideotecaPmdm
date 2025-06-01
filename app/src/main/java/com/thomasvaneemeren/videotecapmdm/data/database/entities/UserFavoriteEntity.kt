package com.thomasvaneemeren.videotecapmdm.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "user_favorites",
    primaryKeys = ["userId", "movieId"],
    foreignKeys = [
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = ["id"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserFavoriteEntity(
    val userId: String,
    val movieId: Int
)