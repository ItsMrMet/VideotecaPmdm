package com.thomasvaneemeren.videotecapmdm.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Main : Screen("main")
    object Add : Screen("add")
    object Detail : Screen("detail/{movieId}") {
        fun passId(id: Int) = "detail/$id"
    }
    object Edit : Screen("edit/{movieId}") {
        fun passId(id: Int) = "edit/$id"
    }
    object Author : Screen("author")
}
