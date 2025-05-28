package com.thomasvaneemeren.videotecapmdm.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thomasvaneemeren.videotecapmdm.ui.screens.SplashScreen
import com.thomasvaneemeren.videotecapmdm.ui.screens.add.AddScreen
import com.thomasvaneemeren.videotecapmdm.ui.screens.author.AuthorScreen
import com.thomasvaneemeren.videotecapmdm.ui.screens.detail.DetailScreen
import com.thomasvaneemeren.videotecapmdm.ui.screens.edit.EditScreen
import com.thomasvaneemeren.videotecapmdm.ui.screens.main.MainScreen
import com.thomasvaneemeren.videotecapmdm.ui.viewmodels.MainViewModel
import com.thomasvaneemeren.videotecapmdm.ui.screens.onboarding.OnboardingScreen
import com.thomasvaneemeren.videotecapmdm.ui.viewmodels.DetailViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        composable(Screen.Onboarding.route) {
            OnboardingScreen(navController = navController)
        }

        composable(Screen.Main.route) {
            val viewModel: MainViewModel = hiltViewModel()
            MainScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(Screen.Add.route) {
            AddScreen(navController = navController)
        }

        composable("detail/{movieId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("movieId")?.toIntOrNull() ?: return@composable
            val viewModel: DetailViewModel = hiltViewModel()
            DetailScreen(movieId = id, navController = navController, viewModel = viewModel)
        }

        composable("edit/{movieId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("movieId")?.toIntOrNull() ?: return@composable
            EditScreen(movieId = id, navController = navController)
        }

        composable(Screen.Author.route) {
            AuthorScreen(navController = navController)
        }

    }
}

