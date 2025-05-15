package com.thomasvaneemeren.videotecapmdm.model

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
    val isFavorite: Boolean,
    val userId: String
)

data class Movie(
    val id: Int = 0,
    val title: String,
    val genre: Genre,
    val synopsis: String,
    val duration: Int,
    val director: String,
    val isFavorite: Boolean,
    val userId: String
)