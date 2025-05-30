package com.thomasvaneemeren.videotecapmdm.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.thomasvaneemeren.videotecapmdm.ui.viewmodels.UserPreferencesViewModel

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: UserPreferencesViewModel = hiltViewModel()
) {
    val userName by viewModel.userName.collectAsState()

    LaunchedEffect(userName) {
        delay(1500)
        if (userName.isBlank()) {
            navController.navigate("onboarding") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            viewModel.login(userName)
            navController.navigate("main") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Videoteca",
            style = MaterialTheme.typography.displayLarge,
            textAlign = TextAlign.Center
        )
    }
}

