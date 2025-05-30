package com.thomasvaneemeren.videotecapmdm.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val genre: String,
    val synopsis: String,
    val duration: Int,
    val director: String,
    val userId: String? = null // 🔧 ESTE CAMPO ES IMPORTANTE
)

data class MovieWithFavorite(
    val movie: MovieEntity,
    val isFavorite: Boolean
)
