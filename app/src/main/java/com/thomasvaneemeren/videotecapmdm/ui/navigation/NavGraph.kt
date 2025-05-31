package com.thomasvaneemeren.videotecapmdm.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.thomasvaneemeren.videotecapmdm.ui.screens.SplashScreen
import com.thomasvaneemeren.videotecapmdm.ui.screens.add.AddScreen
import com.thomasvaneemeren.videotecapmdm.ui.screens.author.AuthorScreen
import com.thomasvaneemeren.videotecapmdm.ui.screens.detail.DetailScreen
import com.thomasvaneemeren.videotecapmdm.ui.screens.edit.EditScreen
import com.thomasvaneemeren.videotecapmdm.ui.screens.main.MainScreen
import com.thomasvaneemeren.videotecapmdm.ui.screens.onboarding.OnboardingScreen
import com.thomasvaneemeren.videotecapmdm.ui.viewmodels.DetailViewModel
import com.thomasvaneemeren.videotecapmdm.ui.viewmodels.MainViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavGraph(
    navController: NavHostController = rememberAnimatedNavController()
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        enterTransition = { fadeIn(animationSpec = tween(500)) },
        exitTransition = { fadeOut(animationSpec = tween(500)) },
        popEnterTransition = { fadeIn(animationSpec = tween(500)) },
        popExitTransition = { fadeOut(animationSpec = tween(500)) }
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

        composable(Screen.Detail.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("movieId")?.toIntOrNull() ?: return@composable
            val viewModel: DetailViewModel = hiltViewModel()
            DetailScreen(movieId = id, navController = navController, viewModel = viewModel)
        }

        composable(Screen.Edit.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("movieId")?.toIntOrNull() ?: return@composable
            EditScreen(movieId = id, navController = navController)
        }

        composable(Screen.Author.route) {
            AuthorScreen(navController = navController, userName = "")
        }
    }
}
