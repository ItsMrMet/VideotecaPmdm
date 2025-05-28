package com.thomasvaneemeren.videotecapmdm.ui.screens.edit

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
import com.thomasvaneemeren.videotecapmdm.ui.components.ScaffoldLayout
import com.thomasvaneemeren.videotecapmdm.ui.viewmodels.AddEditViewModel
import com.thomasvaneemeren.videotecapmdm.ui.viewmodels.UserPreferencesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    movieId: Int,
    navController: NavHostController,
    userPreferencesViewModel: UserPreferencesViewModel = hiltViewModel(),
    addEditViewModel: AddEditViewModel = hiltViewModel()
) {
    val movie by addEditViewModel.movie.collectAsState()
    val userName by userPreferencesViewModel.userName.collectAsState(initial = "")

    var title by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var synopsis by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var director by remember { mutableStateOf("") }
    var isFavorite by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val genres = getGenreList()

    LaunchedEffect(movieId) {
        addEditViewModel.loadMovie(movieId)
    }

    LaunchedEffect(movieId) {
        addEditViewModel.isFavorite(movieId).collect { favorite ->
            isFavorite = favorite
        }
    }

    LaunchedEffect(movie) {
        movie?.let {
            title = it.title
            genre = it.genre.replaceFirstChar { c -> c.uppercase() }
            synopsis = it.synopsis
            duration = it.duration.toString()
            director = it.director
        }
    }

    val isFormValid = title.isNotBlank() && genre.isNotBlank()

    movie?.let {
        ScaffoldLayout(
            userName = userName ?: "",
            navController = navController,
            currentRoute = "edit/$movieId",
            showBackButton = true,
            onBackClick = { navController.popBackStack() }
        ) { paddingValues ->
            Scaffold(
                modifier = Modifier.padding(paddingValues),
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            if (isFormValid) {
                                val durationInt = duration.toIntOrNull() ?: it.duration
                                addEditViewModel.saveMovie(
                                    title = title.trim(),
                                    genre = genre.uppercase(),
                                    synopsis = synopsis.trim(),
                                    duration = durationInt,
                                    director = director.trim(),
                                    isFavorite = isFavorite
                                )
                                navController.popBackStack()
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Text("Guardar")
                    }
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Título") })
                    Spacer(modifier = Modifier.height(8.dp))

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = genre,
                            onValueChange = {},
                            label = { Text("Género") },
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            modifier = Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            genres.forEach { genreOption ->
                                DropdownMenuItem(
                                    text = { Text(genreOption) },
                                    onClick = {
                                        genre = genreOption
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = synopsis, onValueChange = { synopsis = it }, label = { Text("Sinopsis") })
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = duration,
                        onValueChange = { duration = it },
                        label = { Text("Duración (min)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = director, onValueChange = { director = it }, label = { Text("Director") })
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = isFavorite, onCheckedChange = { isFavorite = it })
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Marcar como favorita")
                    }
                }
            }
        }
    } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

