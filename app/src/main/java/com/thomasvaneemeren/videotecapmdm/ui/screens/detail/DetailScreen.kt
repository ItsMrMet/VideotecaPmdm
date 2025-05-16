package com.thomasvaneemeren.videotecapmdm.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.thomasvaneemeren.videotecapmdm.ui.viewmodels.DetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    movieId: Int,
    navController: NavHostController,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val movie by viewModel.getMovieById(movieId).collectAsState(initial = null)

    movie?.let {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = it.title) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            viewModel.deleteMovie(it)
                            navController.popBackStack()
                        }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                        }

                        IconButton(onClick = { navController.navigate("edit/${it.id}") }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar")
                        }
                    }
                )
            },
            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    Text("Género: ${it.genre}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Director: ${it.director}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Duración: ${it.duration} min", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Sinopsis:", style = MaterialTheme.typography.titleMedium)
                    Text(it.synopsis, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Favorita: ${if (it.isFavorite) "Sí" else "No"}", style = MaterialTheme.typography.bodyLarge)
                }
            }
        )
    } ?: Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
        CircularProgressIndicator()
    }
}
