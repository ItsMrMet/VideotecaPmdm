package com.thomasvaneemeren.videotecapmdm.data.model

fun getGenreList(): List<String> =
    Genre.values().map { it.name.lowercase().replaceFirstChar { it.uppercase() } }

enum class Genre {
    DRAMA, COMEDIA, ACCION, TERROR, CIENCIA, FANTASIA
}
