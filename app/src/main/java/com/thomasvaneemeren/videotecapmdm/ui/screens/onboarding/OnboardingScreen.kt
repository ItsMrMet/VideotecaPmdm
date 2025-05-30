package com.thomasvaneemeren.videotecapmdm.ui.screens.onboarding

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.thomasvaneemeren.videotecapmdm.ui.viewmodels.UserPreferencesViewModel
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    navController: NavController,
    viewModel: UserPreferencesViewModel = hiltViewModel()
) {
    var username by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    val content: @Composable ColumnScope.() -> Unit = {
        Text(
            text = "Bienvenido a Videoteca",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Introduce tu nombre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

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
            enabled = username.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
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
