package com.thomasvaneemeren.videotecapmdm.ui.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.thomasvaneemeren.videotecapmdm.ui.viewmodels.UserPreferencesViewModel
import kotlinx.coroutines.delay
import androidx.hilt.navigation.compose.hiltViewModel
import com.thomasvaneemeren.videotecapmdm.R
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: UserPreferencesViewModel = hiltViewModel()
) {
    val userName by viewModel.userName.collectAsState()

    val scope = rememberCoroutineScope()

    // Animación compartida para logo y texto
    val scale = remember { Animatable(0.5f) }
    val alpha = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Fade in + bounce
        alpha.animateTo(1f)
        scale.animateTo(1.1f, tween(300, easing = FastOutLinearInEasing))
        scale.animateTo(0.95f, tween(150, easing = LinearOutSlowInEasing))
        scale.animateTo(1f, tween(150))

        delay(1200)

        // Barrel roll + fade out
        launch {
            rotation.animateTo(360f, tween(600))
        }
        launch {
            alpha.animateTo(0f, tween(600))
        }

        delay(600)
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
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value,
                    rotationZ = rotation.value,
                    alpha = alpha.value
                )
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.videoteca_logo),
                contentDescription = "Logo Videoteca",
                modifier = Modifier.size(180.dp)
            )
            Text(
                text = "Videoteca",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }
    }
}




