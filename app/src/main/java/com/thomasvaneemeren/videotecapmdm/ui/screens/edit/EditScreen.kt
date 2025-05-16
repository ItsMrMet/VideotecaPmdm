package com.thomasvaneemeren.videotecapmdm.ui.screens.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.thomasvaneemeren.videotecapmdm.ui.viewmodels.AddEditViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.thomasvaneemeren.videotecapmdm.data.model.getGenreList
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    movieId: Int,
    navController: NavHostController,
    viewModel: AddEditViewModel = hiltViewModel()
) {
    val movie by viewModel.movie.collectAsState()

    var title by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var genre by remember { mutableStateOf("") }
    val genres = getGenreList()
    var synopsis by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var director by remember { mutableStateOf("") }
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(movieId) {
        viewModel.loadMovie(movieId)
    }

    LaunchedEffect(movie) {
        movie?.let {
            title = it.title
            genre = it.genre.replaceFirstChar { it.uppercase() }
            synopsis = it.synopsis
            duration = it.duration.toString()
            director = it.director
            isFavorite = it.isFavorite
        }
    }

    movie?.let {
        val isFormValid = title.isNotBlank() && genre.isNotBlank()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Editar ${it.title}") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        if (isFormValid) {
                            viewModel.saveMovie(
                                title = title.trim(),
                                genre = genre.uppercase(),
                                synopsis = synopsis.trim(),
                                duration = duration.toIntOrNull() ?: it.duration,
                                director = director.trim(),
                                isFavorite = isFavorite
                            )
                            navController.popBackStack()
                        }
                    }
                ) {
                    Text("Guardar")
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
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
                    label = { Text("Duración") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = director, onValueChange = { director = it }, label = { Text("Director") })

                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isFavorite, onCheckedChange = { isFavorite = it })
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Favorita")
                }
            }
        }
    } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
