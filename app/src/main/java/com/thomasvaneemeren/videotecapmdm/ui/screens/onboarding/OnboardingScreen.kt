// En ui/screens/onboarding/OnboardingScreen.kt
package com.thomasvaneemeren.videotecapmdm.ui.screens.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.thomasvaneemeren.videotecapmdm.ui.viewmodels.OnboardingViewModel
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    navController: NavController,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    var inputName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Bienvenido, por favor ingresa tu nombre")
        TextField(
            value = inputName,
            onValueChange = { inputName = it },
            label = { Text("Nombre") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (inputName.isNotBlank()) {
                    viewModel.saveUserName(inputName.trim())
                    navController.navigate("main") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            }
        ) {
            Text("Guardar y continuar")
        }
    }
}
