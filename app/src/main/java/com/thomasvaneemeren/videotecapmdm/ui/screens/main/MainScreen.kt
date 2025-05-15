package com.thomasvaneemeren.videotecapmdm.ui.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.material3.icons.Icons
import androidx.compose.material3.icons.filled.Add
import androidx.compose.material3.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Movie(
    val id: Int,
    val title: String,
    val genre: String,
    val isFavorite: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    username: String,
    movies: List<Movie> = emptyList(),
    onNavigateToAdd: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onLogout: () -> Unit,
    onNavigateToAuthor: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Videoteca - $username") },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menú")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(text = { Text("Principal") }, onClick = {
                            expanded = false
                            // Navegar a Main (ya está)
                        })
                        DropdownMenuItem(text = { Text("Añadir") }, onClick = {
                            expanded = false
                            onNavigateToAdd()
                        })
                        DropdownMenuItem(text = { Text("Cerrar sesión") }, onClick = {
                            expanded = false
                            onLogout()
                        })
                        DropdownMenuItem(text = { Text("Autor") }, onClick = {
                            expanded = false
                            onNavigateToAuthor()
                        })
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAdd) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Película")
            }
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(movies) { movie ->
                    MovieItem(movie = movie, onClick = { onNavigateToDetail(movie.id) })
                }
            }
        }
    )
}

@Composable
fun MovieItem(movie: Movie, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = movie.title, style = MaterialTheme.typography.titleMedium)
            Text(text = movie.genre, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
