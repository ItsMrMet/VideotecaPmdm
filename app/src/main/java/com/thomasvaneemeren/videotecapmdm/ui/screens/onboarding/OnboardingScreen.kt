package com.thomasvaneemeren.videotecapmdm.ui.screens.onboarding

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.graphicsLayer
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.thomasvaneemeren.videotecapmdm.ui.viewmodels.UserPreferencesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    navController: NavController,
    viewModel: UserPreferencesViewModel = hiltViewModel()
) {
    var username by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    val buttonVisible = username.isNotBlank()
    val buttonAlpha by animateFloatAsState(
        targetValue = if (buttonVisible) 1f else 0.5f,
        animationSpec = tween(500),
        label = "buttonAlpha"
    )

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val content: @Composable ColumnScope.() -> Unit = {
        AnimatedVisibility(
            visible = true,
            enter = slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = tween(600)
            ) + fadeIn(animationSpec = tween(600))
        ) {
            Text(
                text = "Bienvenido a Videoteca",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .graphicsLayer(scaleX = 1.05f, scaleY = 1.05f)
                    .padding(bottom = 16.dp)
            )
        }

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Introduce tu nombre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            interactionSource = interactionSource,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = if (isFocused) MaterialTheme.colorScheme.primary else Color.Gray,
                unfocusedIndicatorColor = Color.LightGray
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        AnimatedVisibility(
            visible = buttonVisible,
            enter = fadeIn(animationSpec = tween(500)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            val interactionSourceBtn = remember { MutableInteractionSource() }
            val isPressed by interactionSourceBtn.collectIsFocusedAsState()
            val scale by animateFloatAsState(
                targetValue = if (isPressed) 0.95f else 1f,
                animationSpec = tween(100),
                label = "buttonScale"
            )

            Button(
                onClick = {
                    val name = username.trim()
                    scope.launch {
                        viewModel.login(name)
                        navController.navigate("main") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer(alpha = buttonAlpha)
                    .scale(scale),
                interactionSource = interactionSourceBtn
            ) {
                Text("Entrar")
            }
        }
    }

    if (isLandscape) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = content
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content
        )
    }
}
