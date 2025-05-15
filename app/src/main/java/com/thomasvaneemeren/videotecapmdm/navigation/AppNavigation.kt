package com.thomasvaneemeren.videotecapmdm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.thomasvaneemeren.videotecapmdm.ui.screens.main.MainScreen
import com.thomasvaneemeren.videotecapmdm.ui.screens.onboarding.OnboardingScreen
import com.thomasvaneemeren.videotecapmdm.ui.screens.splash.SplashScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Destinations.SPLASH
    ) {
        composable(Destinations.SPLASH) { SplashScreen(navController) }
        composable(Destinations.ONBOARDING) { OnboardingScreen(navController) }
        composable(Destinations.MAIN) { MainScreen(navController) }
    }
}