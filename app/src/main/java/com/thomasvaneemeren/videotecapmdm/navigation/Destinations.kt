package com.thomasvaneemeren.videotecapmdm.navigation

object Destinations {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val MAIN = "main"
    const val ADD_MOVIE = "add_movie"

    fun movieDetail(movieId: Int) = "detail/$movieId"
}