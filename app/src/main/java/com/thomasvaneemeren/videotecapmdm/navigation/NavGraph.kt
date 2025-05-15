// En navigation/NavGraph.kt
package com.thomasvaneemeren.videotecapmdm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.thomasvaneemeren.videotecapmdm.ui.screens.main.MainScreen
import com.thomasvaneemeren.videotecapmdm.ui.screens.onboarding.OnboardingScreen
import com.thomasvaneemeren.videotecapmdm.ui.screens.splash.SplashScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") { SplashScreen(navController) }
        composable("onboarding") { OnboardingScreen(navController) }
        composable("main") { MainScreen(navController) }
    }
}