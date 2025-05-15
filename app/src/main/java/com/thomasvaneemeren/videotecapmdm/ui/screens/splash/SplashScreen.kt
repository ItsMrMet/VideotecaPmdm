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
import androidx.navigation.NavController
import com.thomasvaneemeren.videotecapmdm.data.datastore.UserPreferencesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SplashScreen(
    navController: NavController,
    userPreferencesRepository: UserPreferencesRepository = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    var userName by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        userName = userPreferencesRepository.getUserName()
        delay(1500) // splash delay

        if (userName.isNullOrBlank()) {
            navController.navigate("onboarding") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
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
            fontSize = 48.sp,
            textAlign = TextAlign.Center
        )
    }
}
