package com.thomasvaneemeren.videotecapmdm.ui.screens.add

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.thomasvaneemeren.videotecapmdm.data.model.getGenreList
import com.thomasvaneemeren.videotecapmdm.ui.viewmodels.AddEditViewModel
import com.thomasvaneemeren.videotecapmdm.ui.components.ScaffoldLayout
import com.thomasvaneemeren.videotecapmdm.ui.viewmodels.UserPreferencesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    navController: NavHostController,
    addEditViewModel: AddEditViewModel = hiltViewModel(),
    userPreferencesViewModel: UserPreferencesViewModel = hiltViewModel()
) {
    val userName by userPreferencesViewModel.userName.collectAsState(initial = "")
    var title by remember { mutableStateOf("") }
    var expandedGenre by remember { mutableStateOf(false) }
    var genre by remember { mutableStateOf("") }
    val genres = getGenreList()
    var synopsis by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var director by remember { mutableStateOf("") }
    var isFavorite by remember { mutableStateOf(false) }

    val isFormValid = title.isNotBlank() &&
            genre.isNotBlank() &&
            synopsis.isNotBlank() &&
            duration.isNotBlank() &&
            duration.toIntOrNull() != null &&
            director.isNotBlank()

    ScaffoldLayout(
        userName = userName.toString(),
        navController = navController,
        currentRoute = "add",
        showBackButton = true,
        onBackClick = { navController.popBackStack() }
    ) { paddingValues ->
        Scaffold(
            modifier = Modifier.padding(paddingValues)
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(24.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                Text("Añadir Película", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Título") })
                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedGenre,
                    onExpandedChange = { expandedGenre = !expandedGenre }
                ) {
                    OutlinedTextField(
                        value = genre,
                        onValueChange = {},
                        label = { Text("Género") },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGenre)
                        },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedGenre,
                        onDismissRequest = { expandedGenre = false }
                    ) {
                        genres.forEach { genreOption ->
                            DropdownMenuItem(
                                text = { Text(genreOption) },
                                onClick = {
                                    genre = genreOption
                                    expandedGenre = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = synopsis,
                    onValueChange = { synopsis = it },
                    label = { Text("Sinopsis") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = duration,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() }) {
                            duration = newValue
                        }
                    },
                    label = { Text("Duración (min)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = director, onValueChange = { director = it }, label = { Text("Director") })
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = isFavorite, onCheckedChange = { isFavorite = it })
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Marcar como favorita")
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row {
                    Button(
                        onClick = {
                            val dur = duration.toInt()
                            addEditViewModel.saveMovie(
                                title = title.trim(),
                                genre = genre.uppercase(),
                                synopsis = synopsis.trim(),
                                duration = dur,
                                director = director.trim(),
                                isFavorite = isFavorite
                            )
                            navController.navigate("main") {
                                popUpTo("main") { inclusive = true }
                            }
                        },
                        enabled = isFormValid
                    ) {
                        Text("Guardar")
                    }
                    OutlinedButton(onClick = {
                        navController.navigate("main") {
                            popUpTo("main") { inclusive = true }
                        }
                    }) {
                        Text("Cancelar")
                    }
                }
            }
        }
    }
}

