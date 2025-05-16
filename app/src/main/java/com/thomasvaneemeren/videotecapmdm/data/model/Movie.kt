package com.thomasvaneemeren.videotecapmdm.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

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