package com.thomasvaneemeren.videotecapmdm.ui.screens.edit

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun EditScreen(
    movieId: Int,
    navController: NavHostController,
    viewModel: EditViewModel = hiltViewModel()
) {
    val movie by viewModel.getMovieById(movieId).collectAsState(initial = null)

    var title by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var synopsis by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var director by remember { mutableStateOf("") }
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(movie) {
        movie?.let {
            title = it.title
            genre = it.genre
            synopsis = it.synopsis
            duration = it.duration.toString()
            director = it.director
            isFavorite = it.isFavorite
        }
    }

    movie?.let {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Editar ${it.title}") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    viewModel.updateMovie(
                        it.copy(
                            title = title,
                            genre = genre,
                            synopsis = synopsis,
                            duration = duration.toIntOrNull() ?: it.duration,
                            director = director,
                            isFavorite = isFavorite
                        )
                    )
                    navController.popBackStack()
                }) {
                    Text("Guardar")
                }
            }
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Título") })
                OutlinedTextField(value = genre, onValueChange = { genre = it }, label = { Text("Género") })
                OutlinedTextField(value = synopsis, onValueChange = { synopsis = it }, label = { Text("Sinopsis") })
                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = { Text("Duración") },
                    keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions.Default.copy(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
                )
                OutlinedTextField(value = director, onValueChange = { director = it }, label = { Text("Director") })
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Checkbox(checked = isFavorite, onCheckedChange = { isFavorite = it })
                    Text(text = "Favorita")
                }
            }
        }
    } ?: Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
        CircularProgressIndicator()
    }
}

@HiltViewModel
class EditViewModel @javax.inject.Inject constructor(
    private val repository: MovieRepository
) : androidx.lifecycle.ViewModel() {

    fun getMovieById(id: Int) = repository.getMovieByIdFlow(id)

    fun updateMovie(movie: MovieEntity) {
        // Actualizar película en la BD
    }
}
