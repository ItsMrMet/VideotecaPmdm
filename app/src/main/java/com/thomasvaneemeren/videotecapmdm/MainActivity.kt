package com.thomasvaneemeren.videotecapmdm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thomasvaneemeren.videotecapmdm.ui.screens.main.MainScreen
import com.thomasvaneemeren.videotecapmdm.ui.screens.onboarding.OnboardingScreen
import com.thomasvaneemeren.videotecapmdm.ui.screens.splash.SplashScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VideotecaApp()
        }
    }
}

@Composable
fun VideotecaApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("onboarding") {
            OnboardingScreen(navController)
        }
        composable("main") {
            MainScreen(navController)
        }
        /*
        composable("add") {
            AddScreen(navController)
        }
        composable("detail/{movieId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
            if (id != null) {
                DetailScreen(navController, id)
            }
        }
        composable("edit/{movieId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
            if (id != null) {
                EditScreen(navController, id)
            }
        }
        composable("author") {
            AuthorScreen(navController)
        }

         */
    }
}
