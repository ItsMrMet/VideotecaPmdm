package com.thomasvaneemeren.videotecapmdm.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val genre: String,
    val synopsis: String,
    val duration: Int,
    val director: String,
    val isFavorite: Boolean,
    val userId: String // Este campo para identificar el usuario dueño
)
