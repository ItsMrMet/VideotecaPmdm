package com.thomasvaneemeren.videotecapmdm.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

data class MovieDetail(
    val id: Int,
    val title: String,
    val genre: String,
    val synopsis: String,
    val duration: Int,
    val director: String,
    val isFavorite: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    movieId: Int,
    navController: NavHostController,
    viewModel: DetailViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = movie.title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { onDelete(movie.id) }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                    }
                    IconButton(onClick = { onEdit(movie.id) }) {
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
                Text("Género: ${movie.genre}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Director: ${movie.director}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Duración: ${movie.duration} min", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Sinopsis:", style = MaterialTheme.typography.titleMedium)
                Text(movie.synopsis, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Favorita: ${if (movie.isFavorite) "Sí" else "No"}", style = MaterialTheme.typography.bodyLarge)
            }
        }
    )
}
